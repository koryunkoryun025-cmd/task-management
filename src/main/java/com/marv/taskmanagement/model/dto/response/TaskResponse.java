package com.marv.taskmanagement.model.dto.response;

import com.marv.taskmanagement.model.entity.TaskEntity;
import com.marv.taskmanagement.model.enums.TaskStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate deadline;

    public TaskResponse(TaskEntity taskEntity) {
        this.id = taskEntity.getId();
        this.title = taskEntity.getTitle();
        this.description = taskEntity.getDescription();
        this.status = taskEntity.getStatus();
        this.deadline = taskEntity.getDeadline();
    }
}
