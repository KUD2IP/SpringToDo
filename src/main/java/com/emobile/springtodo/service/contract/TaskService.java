package com.emobile.springtodo.service.contract;

import com.emobile.springtodo.model.dto.response.PageResponse;
import com.emobile.springtodo.model.dto.request.TaskRequest;
import com.emobile.springtodo.model.dto.response.TaskResponse;

public interface TaskService {
    PageResponse<TaskResponse> getAllTasks(int page, int size);
    TaskResponse getTaskById(Long id);
    TaskResponse addTask(TaskRequest task);
    TaskResponse updateTask(Long id, TaskRequest task);
    void deleteTask(Long id);
}
