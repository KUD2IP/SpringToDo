package com.emobile.springtodo.service;

import com.emobile.springtodo.model.dto.response.PageResponse;
import com.emobile.springtodo.model.dto.request.TaskRequest;
import com.emobile.springtodo.model.dto.response.TaskResponse;
import com.emobile.springtodo.model.entity.Status;
import com.emobile.springtodo.model.entity.Task;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.model.mapper.contract.TaskMapper;
import com.emobile.springtodo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private LocalDateTime now = LocalDateTime.now();

    private Task task1 = Task.builder()
            .id(1L)
            .title("Title1")
            .description("Description1")
            .status(Status.IN_PROGRESS)
            .build();

    private Task task2 = Task.builder()
            .id(2L)
            .title("Title2")
            .description("Description2")
            .status(Status.IN_PROGRESS)
            .build();

    private TaskResponse taskResponse1 = TaskResponse.builder()
            .id(1L)
            .title("Title1")
            .description("Description1")
            .status(Status.IN_PROGRESS)
            .build();

    private TaskResponse taskResponse2 = TaskResponse.builder()
            .id(2L)
            .title("Title2")
            .description("Description2")
            .status(Status.IN_PROGRESS)
            .build();

    private TaskRequest taskRequest1 = TaskRequest.builder()
            .title("Title1")
            .description("Description1")
            .status(Status.IN_PROGRESS)
            .build();

    private TaskRequest taskRequest2 = TaskRequest.builder()
            .title("Title2")
            .description("Description2")
            .status(Status.IN_PROGRESS)
            .build();

    @Test
    void testGetAllTasks() {
        List<Task> tasks = List.of(task1, task2);

        when(taskMapper.toDtoResponse(task1)).thenReturn(taskResponse1);
        when(taskMapper.toDtoResponse(task2)).thenReturn(taskResponse2);
        when(taskRepository.findAll(10, 0)).thenReturn(tasks);

        PageResponse<TaskResponse> tasksRes = taskService.getAllTasks(1, 10);

        assertEquals(2, tasksRes.getTasks().size());
        assertEquals("Title1", tasksRes.getTasks().getFirst().getTitle());
        verify(taskRepository, times(1)).findAll(10, 0);
    }

    @Test
    void testGetTask() {

        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task1));
        when(taskMapper.toDtoResponse(task1)).thenReturn(taskResponse1);

        TaskResponse task = taskService.getTaskById(1L);

        assertEquals("Title1", task.getTitle());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskNotFound() {

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        when(taskMapper.toDtoResponse(task1)).thenReturn(taskResponse1);

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testAddTask() {

        when(taskMapper.toEntity(taskRequest1)).thenReturn(task1);
        when(taskMapper.toDtoResponse(task1)).thenReturn(taskResponse1);

        TaskResponse task = taskService.addTask(taskRequest1);

        assertEquals("Title1", task.getTitle());

        verify(taskRepository, times(1)).save(task1);

    }

    @Test
    void testDeleteTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));

        verify(taskRepository, times(1)).findById(1L);

    }
}
