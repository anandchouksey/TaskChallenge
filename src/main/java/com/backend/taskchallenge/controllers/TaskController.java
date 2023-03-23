package com.backend.taskchallenge.controllers;

import com.backend.taskchallenge.model.Task;
import com.backend.taskchallenge.service.FileService;
import com.backend.taskchallenge.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class TaskController {

    private final TaskService taskService;
    private final FileService fileService;

    @Autowired
    public TaskController(final TaskService taskService,
                          final FileService fileService) {
        this.taskService = taskService;
        this.fileService = fileService;
    }

    @GetMapping("/v1/api/tasks")
    public List<Task> listTasks() {
        return taskService.listTasks();
    }

    @PostMapping("/v1/api/tasks")
    public Task createTask(@RequestBody @Valid final Task Task) {
        return taskService.createTask(Task);
    }

    @GetMapping("/v1/api/tasks/{taskId}")
    public Task getTask(@PathVariable final String taskId) {
        return taskService.getTask(taskId);
    }

    @PutMapping("/v1/api/tasks/{taskId}")
    public Task updateTask(@PathVariable final String taskId,
                                            @RequestBody @Valid final Task Task) {
        return taskService.update(taskId, Task);
    }

    @DeleteMapping("/v1/api/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable final String taskId) {
        taskService.delete(taskId);
    }

    @GetMapping("/v1/api/tasks/result/{taskId}")
    public ResponseEntity<FileSystemResource> getResult(@PathVariable final String taskId) {
        return fileService.getTaskResult(taskId);
    }

    @PostMapping("/v1/api/tasks/execute/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void executeTask(@PathVariable final String taskId) {
        taskService.executeTask(taskId);
    }

    @PostMapping("/v2/api/tasks/execute/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void triggerExecution(@PathVariable final String taskId) {
        taskService.triggerTaskExecution(taskId);
    }

    @PostMapping("/v2/api/tasks/cancel/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTaskExecution(@PathVariable final String taskId) {
        taskService.cancelTaskExecution(taskId);
    }

    @GetMapping("/v2/api/tasks/status/{taskId}")
    public Task getExecutionStatus(@PathVariable final String taskId) {
        return taskService.getExecutionStatus(taskId);
    }

}
