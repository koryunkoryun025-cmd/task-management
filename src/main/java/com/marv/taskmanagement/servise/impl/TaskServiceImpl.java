package com.marv.taskmanagement.servise.impl;

import com.marv.taskmanagement.exceptions.ResourceNotFoundException;
import com.marv.taskmanagement.model.dto.request.CreateTaskRequest;
import com.marv.taskmanagement.model.dto.request.UpdateTaskRequest;
import com.marv.taskmanagement.model.dto.response.TaskResponse;
import com.marv.taskmanagement.model.entity.TaskEntity;
import com.marv.taskmanagement.repository.TaskRepository;
import com.marv.taskmanagement.servise.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public TaskResponse create(CreateTaskRequest request) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(request.getTitle());
        taskEntity.setDescription(request.getDescription());
        taskEntity.setDeadline(request.getDeadline());

        TaskEntity saved = taskRepository.save(taskEntity);
        return new TaskResponse(saved);
    }

    @Override
    public List<TaskResponse> getAll() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponse::new)
                .toList();
    }

    @Override
    public TaskResponse getById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return new TaskResponse(taskEntity);
    }

    @Override
    public TaskResponse update(Long id, UpdateTaskRequest request) {
        TaskEntity existing = taskRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setStatus(request.getStatus());
        existing.setDeadline(request.getDeadline());

        TaskEntity saved = taskRepository.save(existing);
        return new TaskResponse(saved);
    }

    @Override
    public void delete(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskRepository.deleteById(id);
    }
}
