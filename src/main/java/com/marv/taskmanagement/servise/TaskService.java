package com.marv.taskmanagement.servise;

import com.marv.taskmanagement.model.dto.request.CreateTaskRequest;
import com.marv.taskmanagement.model.dto.request.UpdateTaskRequest;
import com.marv.taskmanagement.model.dto.response.TaskResponse;
import com.marv.taskmanagement.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    TaskResponse create(CreateTaskRequest request);

    Page<TaskResponse> getAll(TaskStatus status, Pageable pageable);

    TaskResponse getById(Long id);

    TaskResponse update(Long id, UpdateTaskRequest request);

    void delete(Long id);
}
