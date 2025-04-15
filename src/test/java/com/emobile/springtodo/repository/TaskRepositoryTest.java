package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.entity.Status;
import com.emobile.springtodo.model.entity.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@Import(AbstractTestContainers.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class TaskRepositoryTest extends AbstractTestContainers {

    @Autowired
    private TaskRepository taskRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private final LocalDateTime now = LocalDateTime.now();

    private Task createTask(String title, String description) {
        return Task.builder()
                .title(title)
                .description(description)
                .status(Status.IN_PROGRESS)
                .createdAt(now)
                .build();
    }

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("TRUNCATE TABLE task RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    void testFindAll() {
        Task task1 = createTask("Title1", "Description1");
        Task task2 = createTask("Title2", "Description2");

        taskRepository.save(task1);
        taskRepository.save(task2);
        entityManager.flush();

        List<Task> tasks = taskRepository.findAll(10, 0);

        assertEquals(2, tasks.size());
    }

    @Test
    void testFindById() {
        Task task = createTask("Title1", "Description1");
        taskRepository.save(task);
        entityManager.flush();

        Task found = taskRepository.findById(task.getId()).orElseThrow();

        assertEquals(1L, found.getId());
        assertEquals("Title1", found.getTitle());
        assertEquals(Status.IN_PROGRESS, found.getStatus());
    }

    @Test
    void testSave() {
        Task task = createTask("Title1", "Description1");
        Task saved = taskRepository.save(task);
        entityManager.flush();

        assertEquals(1L, saved.getId());
        assertEquals(1, taskRepository.count());
    }

    @Test
    void testUpdate() {
        Task task = createTask("Title1", "Description1");
        taskRepository.save(task);
        entityManager.flush();

        task.setTitle("UpdatedTitle");
        task.setStatus(Status.COMPLETED);
        Task updated = taskRepository.save(task);
        entityManager.flush();

        Task found = entityManager.find(Task.class, updated.getId());
        assertEquals(1L, found.getId());
        assertEquals("UpdatedTitle", found.getTitle());
        assertEquals(Status.COMPLETED, found.getStatus());
    }

    @Test
    void testDelete() {
        Task task = createTask("Title1", "Description1");
        taskRepository.save(task);
        entityManager.flush();

        taskRepository.deleteById(task.getId());
        entityManager.flush();

        assertFalse(taskRepository.existsById(task.getId()));
        assertEquals(0, taskRepository.count());
    }
}