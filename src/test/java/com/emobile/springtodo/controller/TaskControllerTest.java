package com.emobile.springtodo.controller;

import com.emobile.springtodo.contreoller.TaskController;
import com.emobile.springtodo.dto.PageResponse;
import com.emobile.springtodo.dto.TaskDtoRequest;
import com.emobile.springtodo.dto.TaskDtoResponse;
import com.emobile.springtodo.entity.Status;
import com.emobile.springtodo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    private final LocalDateTime now = LocalDateTime.now();

    private final TaskDtoRequest taskDtoRequest = TaskDtoRequest.builder()
            .title("title")
            .description("description")
            .status(Status.IN_PROGRESS)
            .build();

    private final TaskDtoResponse task = TaskDtoResponse.builder()
            .id(1L)
            .title("title")
            .description("description")
            .status(Status.IN_PROGRESS)
            .createdAt(now.toString())
            .build();


    @Test
    void testAddTask() throws Exception {

        when(taskService.addTask(any(TaskDtoRequest.class))).thenReturn(task);

        mockMvc.perform(post("/task/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskDtoRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("title"));

        verify(taskService, times(1)).addTask(any(TaskDtoRequest.class));
    }


    @Test
    void testGetTask() throws Exception {

        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/task/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("title"));

        verify(taskService, times(1)).getTaskById(1L);

    }

    @Test
    void testGetAllTasks() throws Exception {
        TaskDtoResponse task2 = TaskDtoResponse.builder()
                .id(2L)
                .title("title2")
                .description("description2")
                .status(Status.IN_PROGRESS)
                .createdAt(now.toString())
                .build();
        PageResponse<TaskDtoResponse> tasks = new PageResponse<>(List.of(task, task2), 1, 2);

        when(taskService.getAllTasks(1, 2)).thenReturn(tasks);

        mockMvc.perform(get("/task")
                        .param("page", "1")
                        .param("size", "2")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tasks.length()").value(2))
                .andExpect(jsonPath("$.tasks[0].title").value("title"))
                .andExpect(jsonPath("$.tasks[1].title").value("title2"));

        verify(taskService, times(1)).getAllTasks(1, 2);
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskDtoResponse updateTask = TaskDtoResponse.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .status(Status.IN_PROGRESS)
                .createdAt(now.toString())
                .build();

        when(taskService.updateTask(eq(1L), any(TaskDtoRequest.class))).thenReturn(updateTask);

        mockMvc.perform(patch("/task/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TaskDtoRequest.builder().title("titleUpdate").description("descriptionUpdate").build()))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("titleUpdate"))
                .andExpect(jsonPath("$.description").value("descriptionUpdate"))
                .andExpect(jsonPath("$.status").value(Status.IN_PROGRESS.toString()));

        verify(taskService, times(1)).updateTask(eq(1L), any(TaskDtoRequest.class));
    }

    @Test
    void testDeleteTask() throws Exception {

        doNothing().when(taskService).deleteTask(anyLong());

        mockMvc.perform(delete("/task/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(anyLong());
    }
}
