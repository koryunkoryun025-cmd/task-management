package com.marv.taskmanagement.model.dto.request;

import com.marv.taskmanagement.model.enums.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UpdateTaskRequest {

    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private TaskStatus status;

    @Future(message = "Deadline must be in the future")
    private LocalDate deadline;
}
