package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.PageResponse;
import com.emobile.springtodo.dto.TaskDtoRequest;
import com.emobile.springtodo.dto.TaskDtoResponse;

public interface TaskService {
    PageResponse<TaskDtoResponse> getAllTasks(int page, int size);
    TaskDtoResponse getTaskById(Long id);
    TaskDtoResponse addTask(TaskDtoRequest task);
    TaskDtoResponse updateTask(Long id, TaskDtoRequest task);
    void deleteTask(Long id);
}
