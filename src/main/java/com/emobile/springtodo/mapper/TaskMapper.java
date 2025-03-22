package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.TaskDtoRequest;
import com.emobile.springtodo.dto.TaskDtoResponse;
import com.emobile.springtodo.entity.Task;

public interface TaskMapper {

    Task toEntity(TaskDtoRequest taskDtoRequest);
    TaskDtoResponse toDtoResponse(Task task);

}
