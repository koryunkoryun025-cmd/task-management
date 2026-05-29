package com.marv.taskmanagement.servise;

import com.marv.taskmanagement.model.dto.request.CreateTaskRequest;
import com.marv.taskmanagement.model.dto.request.UpdateTaskRequest;
import com.marv.taskmanagement.model.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {

    TaskResponse create(CreateTaskRequest request);

    List<TaskResponse> getAll();

    TaskResponse getById(Long id);

    TaskResponse update(Long id, UpdateTaskRequest request);

    void delete(Long id);
}
