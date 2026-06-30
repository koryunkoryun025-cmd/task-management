package com.marv.taskmanagement.servise.impl;

import com.marv.taskmanagement.constants.ErrorMessages;
import com.marv.taskmanagement.exceptions.ResourceNotFoundException;
import com.marv.taskmanagement.mapper.TaskMapper;
import com.marv.taskmanagement.model.dto.request.CreateTaskRequest;
import com.marv.taskmanagement.model.dto.request.UpdateTaskRequest;
import com.marv.taskmanagement.model.dto.response.TaskResponse;
import com.marv.taskmanagement.model.entity.TaskEntity;
import com.marv.taskmanagement.model.entity.UserEntity;
import com.marv.taskmanagement.model.enums.TaskStatus;
import com.marv.taskmanagement.repository.TaskRepository;
import com.marv.taskmanagement.repository.UserRepository;
import com.marv.taskmanagement.servise.TaskService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;

    private UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
    }

    @Transactional
    @Override
    public TaskResponse create(CreateTaskRequest request) {
        TaskEntity taskEntity = taskMapper.toEntity(request);
        taskEntity.setUser(getCurrentUser());
        TaskEntity saved = taskRepository.save(taskEntity);
        return taskMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TaskResponse> getAll(TaskStatus status, Pageable pageable) {
        UserEntity currentUser = getCurrentUser();
        if (status != null) {
            return taskRepository.findByUserAndStatus(currentUser, status, pageable)
                    .map(taskMapper:: toResponse);
        }
        return taskRepository.findByUser(currentUser, pageable)
                .map(taskMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public TaskResponse getById(Long id) {
        UserEntity currentUser = getCurrentUser();
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.TASK_NOT_FOUND + id));

        if (!taskEntity.getUser().getId().equals(currentUser.getId())) {
            throw  new ResourceNotFoundException(ErrorMessages.TASK_NOT_FOUND + id);
        }
        return taskMapper.toResponse(taskEntity);
    }

    @Transactional
    @Override
    public TaskResponse update(Long id, UpdateTaskRequest request) {
        UserEntity currentUser = getCurrentUser();
        TaskEntity existing = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.TASK_NOT_FOUND + id));

        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException(ErrorMessages.TASK_NOT_FOUND + id);
        }

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setStatus(request.getStatus());
        existing.setDeadline(request.getDeadline());

        TaskEntity saved = taskRepository.save(existing);
        return taskMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        UserEntity currentUser = getCurrentUser();
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.TASK_NOT_FOUND + id));

        if (!taskEntity.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException(ErrorMessages.TASK_NOT_FOUND + id);
        }
        taskRepository.deleteById(id);
    }
}
