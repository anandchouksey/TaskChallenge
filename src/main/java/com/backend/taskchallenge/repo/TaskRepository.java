package com.backend.taskchallenge.repo;

import com.backend.taskchallenge.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository  extends JpaRepository<Task, String> {

}
