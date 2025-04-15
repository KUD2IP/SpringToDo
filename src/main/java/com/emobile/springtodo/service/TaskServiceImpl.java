package com.emobile.springtodo.service;

import com.emobile.springtodo.model.dto.response.PageResponse;
import com.emobile.springtodo.model.dto.request.TaskRequest;
import com.emobile.springtodo.model.dto.response.TaskResponse;
import com.emobile.springtodo.model.entity.Task;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.model.mapper.contract.TaskMapper;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.service.contract.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<TaskResponse> getAllTasks(int page, int size) {

        int offset = (page - 1) * size;

        List<TaskResponse> tasks = taskRepository.findAll(size, offset)
                .stream()
                .map(taskMapper::toDtoResponse)
                .toList();

        return new PageResponse<>(tasks, page, size);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tasks", key = "#id")
    @Override
    public TaskResponse getTaskById(Long id) {

        return taskMapper.toDtoResponse(
                taskRepository.findById(id)
                .orElseThrow( () -> new TaskNotFoundException("Task not found")));
    }

    @Transactional
    @Override
    public TaskResponse addTask(TaskRequest task) {

        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            task.setTitle("Task Title");
        }

        Task taskEntity = taskMapper.toEntity(task);
        taskRepository.save(taskEntity);

        return taskMapper.toDtoResponse(taskEntity);
    }

    @Transactional
    @CachePut(value = "tasks", key = "#id")
    @Override
    public TaskResponse updateTask(Long id, TaskRequest task) {

        Task taskEntity = taskRepository.findById(id)
                .orElseThrow( () -> new TaskNotFoundException("Task not found"));

        taskEntity.setDescription(
                Optional.ofNullable(task.getDescription())
                        .filter(s -> !s.isEmpty())
                        .orElse(taskEntity.getDescription()));

        taskEntity.setTitle(
                Optional.ofNullable(task.getTitle())
                        .filter(s -> !s.isEmpty())
                        .orElse(taskEntity.getTitle()));

        taskEntity.setStatus(
                Optional.ofNullable(task.getStatus())
                        .orElse(taskEntity.getStatus()));

        taskRepository.save(taskEntity);

        return taskMapper.toDtoResponse(taskEntity);
    }

    @Transactional
    @CacheEvict(value = "tasks", key = "#id")
    @Override
    public void deleteTask(Long id) {

        taskRepository.findById(id)
                .orElseThrow( () -> new TaskNotFoundException("Task not found"));

        taskRepository.deleteById(id);
    }
}
