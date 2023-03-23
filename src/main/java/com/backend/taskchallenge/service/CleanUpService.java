package com.backend.taskchallenge.service;

import com.backend.taskchallenge.model.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Calendar;

@Service
public class CleanUpService {
    @Autowired
    private TaskService taskService;

    @Scheduled(cron = "0 0 1 * * *")
    public void cleanupOldTasks() {
        final Calendar calendar= Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        taskService.listTasks()
                .stream()
                .filter(task -> task.getCreationDate().before(calendar.getTime()) && TaskStatus.CREATED==task.getTaskStatus())
                .forEach(task ->
                        taskService.delete(task.getId()));
    }

}
