package com.marv.taskmanagement.repository;

import com.marv.taskmanagement.model.entity.TaskEntity;
import com.marv.taskmanagement.model.entity.UserEntity;
import com.marv.taskmanagement.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Page<TaskEntity> findByStatus(TaskStatus status, Pageable pageable);

    Page<TaskEntity> findByUser(UserEntity user, Pageable pageable);

    Page<TaskEntity> findByUserAndStatus(UserEntity user, TaskStatus status, Pageable pageable);
}



