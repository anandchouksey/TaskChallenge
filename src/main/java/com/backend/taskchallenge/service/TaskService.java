package com.backend.taskchallenge.service;

import com.backend.taskchallenge.exceptions.InternalException;
import com.backend.taskchallenge.exceptions.NotFoundException;
import com.backend.taskchallenge.model.Task;
import com.backend.taskchallenge.model.TaskStatus;
import com.backend.taskchallenge.repo.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class TaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    @Autowired
    private AsyncService asyncService;
    private Map<String, CompletableFuture<Task>> completableFutureMap = new ConcurrentHashMap<>();

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private FileService fileService;

    public List<Task> listTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        task.setId(null);
        task.setCreationDate(new Date());
        return taskRepository.save(task);
    }

    public Task getTask(String taskId) {
        return get(taskId);
    }

    public Task update(String taskId, Task projectGenerationTask) {
        Task existingTask = get(taskId);
        existingTask.setCreationDate(projectGenerationTask.getCreationDate());
        existingTask.setName(projectGenerationTask.getName());
        existingTask.setX(projectGenerationTask.getX());
        existingTask.setY(projectGenerationTask.getY());
        existingTask.setTaskStatus(projectGenerationTask.getTaskStatus());
        return taskRepository.save(existingTask);
    }

    public void delete(String taskId) {
        taskRepository.deleteById(taskId);
    }

    public void executeTask(String taskId) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("challenge.zip");
        if (url == null) {
            throw new InternalException("Zip file not found");
        }
        try {
            fileService.storeResult(taskId, url);
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private Task get(String taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        return task.orElseThrow(NotFoundException::new);
    }

    public CompletableFuture<Task> triggerTaskExecution(String taskId) {
        try {
            Task task = get(taskId);
            LOGGER.info("Task submitted for execution with uuid '{}' started with x= {} and y= {} ",
                    task.getId(), task.getX(), task.getY());
            saveExecutionStatusInDB(task);
            CompletableFuture<Task> taskCompletableFuture = asyncService.triggerTaskExecution(task);
            completableFutureMap.putIfAbsent(taskId, taskCompletableFuture);
            Optional.ofNullable(taskCompletableFuture.get())
                    .ifPresentOrElse(taskGet -> taskRepository.save(taskGet), NotFoundException::new);
            return taskCompletableFuture;
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Task Execution API error: The task with uuid '{}' was interrupted", taskId);
            throw new RuntimeException(e);
        }
    }

    private void saveExecutionStatusInDB(Task task) {
        task.setTaskStatus(TaskStatus.IN_EXECUTION);
        taskRepository.save(task);
    }

    public Task getExecutionStatus(String taskId) {
        Task enquiredTask = null;
        try {
            if(completableFutureMap.containsKey(taskId)) {
                LOGGER.info("Running Task status is requested with uuid '{}' ", taskId);
                CompletableFuture<Task> submittedThreadpoolTask = completableFutureMap.get(taskId);
                enquiredTask = submittedThreadpoolTask.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Task ExecutionStatus API error: The task with uuid '{}' was interrupted", taskId);
            throw new RuntimeException(e);
        }
        return enquiredTask;
    }

    public void cancelTaskExecution(String taskId) {
        try {
            if(completableFutureMap.containsKey(taskId)) {
                LOGGER.info("Task requested to be cancelled uuid '{}' ", taskId);
                CompletableFuture<Task> submittedTask = completableFutureMap.get(taskId);
                submittedTask.cancel(true);
                Optional.ofNullable(submittedTask.get()).ifPresentOrElse(task -> {
                    task.setTaskStatus(TaskStatus.CANCELLED);
                    update(taskId, task);
                }, NotFoundException::new);
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Task Cancel API error: The task with uuid '{}' could not be cancelled", taskId);
            throw new RuntimeException(e);
        }
    }
}
