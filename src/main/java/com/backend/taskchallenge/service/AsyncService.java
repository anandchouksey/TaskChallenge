package com.backend.taskchallenge.service;

import com.backend.taskchallenge.model.Task;
import com.backend.taskchallenge.model.TaskStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AsyncService {

    @Value("${task.time.increment.frequency}")
    private int timeIncrementFrequency;

    @Async("asyncExecutor")
    public CompletableFuture<Task> triggerTaskExecution(final Task submittedTask) throws InterruptedException {
        startCounter(submittedTask);
        return CompletableFuture.completedFuture(submittedTask);
    }

    private void startCounter(final Task submittedTask) {
        final AtomicInteger variableX = new AtomicInteger(submittedTask.getX());
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int counter = variableX.get();
            @Override
            public void run() {
                submittedTask.setX(variableX.getAndIncrement());
                counter++;
                if (counter >= submittedTask.getY() - variableX.get()){
                    submittedTask.setX(submittedTask.getY());
                    submittedTask.setTaskStatus(TaskStatus.COMPLETED);
                    timer.cancel();
                }
            }
        }, 0, timeIncrementFrequency);
    }

}
