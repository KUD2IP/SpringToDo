package com.emobile.springtodo.model.mapper.contract;

import com.emobile.springtodo.model.dto.request.TaskRequest;
import com.emobile.springtodo.model.dto.response.TaskResponse;
import com.emobile.springtodo.model.entity.Task;

public interface TaskMapper {

    Task toEntity(TaskRequest taskRequest);
    TaskResponse toDtoResponse(Task task);

}
