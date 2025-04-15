package com.emobile.springtodo.model.mapper;

import com.emobile.springtodo.model.dto.request.TaskRequest;
import com.emobile.springtodo.model.dto.response.TaskResponse;
import com.emobile.springtodo.model.entity.Status;
import com.emobile.springtodo.model.entity.Task;
import com.emobile.springtodo.model.mapper.contract.TaskMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskMapperImpl implements TaskMapper {
    @Override
    public Task toEntity(TaskRequest taskRequest) {
        return Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(Status.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public TaskResponse toDtoResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt().toString())
                .build();
    }
}
