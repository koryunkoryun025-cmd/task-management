package com.marv.taskmanagement.mapper;

import com.marv.taskmanagement.model.dto.request.CreateTaskRequest;
import com.marv.taskmanagement.model.dto.response.TaskResponse;
import com.marv.taskmanagement.model.entity.TaskEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponse toResponse(TaskEntity entity);

    TaskEntity toEntity(CreateTaskRequest request);
}
