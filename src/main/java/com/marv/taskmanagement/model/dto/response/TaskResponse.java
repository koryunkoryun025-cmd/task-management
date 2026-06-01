package com.marv.taskmanagement.model.dto.response;

import com.marv.taskmanagement.model.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate deadline;
}
