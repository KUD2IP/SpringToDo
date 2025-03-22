package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.TaskDtoRequest;
import com.emobile.springtodo.dto.TaskDtoResponse;
import com.emobile.springtodo.entity.Status;
import com.emobile.springtodo.entity.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskMapperImpl implements TaskMapper {
    @Override
    public Task toEntity(TaskDtoRequest taskDtoRequest) {
        return Task.builder()
                .title(taskDtoRequest.getTitle())
                .description(taskDtoRequest.getDescription())
                .status(Status.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public TaskDtoResponse toDtoResponse(Task task) {
        return TaskDtoResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt().toString())
                .build();
    }
}
