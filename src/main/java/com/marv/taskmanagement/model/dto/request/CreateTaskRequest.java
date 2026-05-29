package com.marv.taskmanagement.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateTaskRequest {

    @NotBlank
    private String title;
    private String description;
    private LocalDate deadline;
}
