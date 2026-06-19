package com.marv.taskmanagement.servise.impl;

import com.marv.taskmanagement.exceptions.ResourceNotFoundException;
import com.marv.taskmanagement.mapper.TaskMapper;
import com.marv.taskmanagement.model.dto.request.CreateTaskRequest;
import com.marv.taskmanagement.model.dto.request.UpdateTaskRequest;
import com.marv.taskmanagement.model.dto.response.TaskResponse;
import com.marv.taskmanagement.model.entity.TaskEntity;
import com.marv.taskmanagement.model.enums.TaskStatus;
import com.marv.taskmanagement.repository.TaskRepository;
import com.marv.taskmanagement.servise.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    @Override
    public TaskResponse create(CreateTaskRequest request) {
        TaskEntity taskEntity = taskMapper.toEntity(request);
        TaskEntity saved = taskRepository.save(taskEntity);
        return taskMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TaskResponse> getAll(TaskStatus status, Pageable pageable) {
        if (status != null) {
            return taskRepository.findByStatus(status, pageable)
                    .map(taskMapper:: toResponse);
        }
        return taskRepository.findAll(pageable)
                .map(taskMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public TaskResponse getById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.toResponse(taskEntity);
    }

    @Transactional
    @Override
    public TaskResponse update(Long id, UpdateTaskRequest request) {
        TaskEntity existing = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setStatus(request.getStatus());
        existing.setDeadline(request.getDeadline());

        TaskEntity saved = taskRepository.save(existing);
        return taskMapper.toResponse((saved));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskRepository.deleteById(id);
    }
}
