package com.backend.taskchallenge.service;

import com.backend.taskchallenge.exceptions.InternalException;
import com.backend.taskchallenge.model.Task;
import com.backend.taskchallenge.repo.TaskRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

@Service
public class FileService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;

    public ResponseEntity<FileSystemResource> getTaskResult(final String taskId) {
        final Task task = taskService.getTask(taskId);
        final File inputFile = new File(task.getStoragePath());

        if (!inputFile.exists()) {
            throw new InternalException("File not generated yet");
        }

        final HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        respHeaders.setContentDispositionFormData("attachment", "challenge.zip");
        return new ResponseEntity<>(new FileSystemResource(inputFile), respHeaders, HttpStatus.OK);
    }

    public void storeResult(final String taskId, final URL url) throws IOException {
        final Task task = taskService.getTask(taskId);
        final File outputFile = File.createTempFile(taskId, ".zip");
        outputFile.deleteOnExit();
        task.setStoragePath(outputFile.getAbsolutePath());
        taskRepository.save(task);
        try (final InputStream inputStream = url.openStream();
             final OutputStream outputStream = new FileOutputStream(outputFile)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

}
