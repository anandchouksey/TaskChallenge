package com.backend.taskchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskChallengeApplication.class, args);
    }
}
