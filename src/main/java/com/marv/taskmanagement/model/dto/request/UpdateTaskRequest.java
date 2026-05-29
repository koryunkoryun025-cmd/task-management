package com.marv.taskmanagement.model.dto.request;

import com.marv.taskmanagement.model.enums.TaskStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateTaskRequest {

    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate deadline;
}
