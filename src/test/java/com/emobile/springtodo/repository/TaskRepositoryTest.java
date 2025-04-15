package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.entity.Status;
import com.emobile.springtodo.model.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class TaskRepositoryTest extends AbstractTestContainers{

    private final TaskRepository taskRepository;

    public TaskRepositoryTest() {
        this.taskRepository = new TaskRepositoryImpl(new JdbcTemplate(dataSource));
    }

    private LocalDateTime now = LocalDateTime.now();

    private Task task = Task.builder()
            .id(null)
            .title("Title1")
            .description("Description1")
            .status(Status.IN_PROGRESS)
            .createdAt(now)
            .build();

    @Test
    void testFindAll() {
        taskRepository.save(task);

        taskRepository.save(Task.builder()
                        .id(null)
                .title("Title2")
                .description("Description2")
                .status(Status.IN_PROGRESS)
                .createdAt(now)
                .build()
        );

        List<Task> todos = taskRepository.findAll(10, 0);

        assertEquals(2, todos.size());
        assertEquals("Title1", todos.get(0).getTitle());
        assertEquals("Title2", todos.get(1).getTitle());
    }

    @Test
    void testFindById() {
        taskRepository.save(task);

        Task tasks = taskRepository.findById(task.getId()).get();

        assertEquals(1L, tasks.getId() );
        assertEquals("Title1", tasks.getTitle());
        assertEquals("Description1", tasks.getDescription());
        assertEquals(Status.IN_PROGRESS, tasks.getStatus());
    }

    @Test
    void testSave() {
        taskRepository.save(task);

        assertEquals(1L, task.getId());
    }

    @Test
    void testUpdate() {
        taskRepository.save(task);

        Task updatedTask = Task.builder()
                .id(task.getId())
                .title("NewTitle")
                .description(task.getDescription())
                .status(Status.COMPLETED)
                .createdAt(task.getCreatedAt())
                .build();

        taskRepository.update(updatedTask);

        Task findTask = taskRepository.findById(updatedTask.getId()).get();

        assertEquals(1L, findTask.getId());
        assertEquals("NewTitle", findTask.getTitle());
        assertEquals("Description1", findTask.getDescription());
        assertEquals(Status.COMPLETED, findTask.getStatus());
    }

    @Test
    void testDelete() {
        taskRepository.save(task);

        taskRepository.deleteById(task.getId());

        Optional<Task> findTask = taskRepository.findById(task.getId());

        assertTrue(findTask.isEmpty());
    }
}
