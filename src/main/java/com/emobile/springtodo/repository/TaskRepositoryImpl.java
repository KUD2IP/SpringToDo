package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Status;
import com.emobile.springtodo.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Task task) {
        String sql = "INSERT INTO task_t (title, description, status, created_at) VALUES (?,?,?,?) RETURNING id";
        Long generatedId = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getCreatedAt()
        );

        task.setId(generatedId);
    }

    @Override
    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM task_t WHERE id = ?";
        List<Task> task = jdbcTemplate.query(sql, rowMapper(), id);
        return task.stream().findFirst();
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT * FROM task_t ORDER BY id ASC";
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM task_t WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void update(Task task) {
        String sql = "UPDATE task_t SET title = ?, description = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getId()
        );
    }

    @Override
    public List<Task> findAll(int limit, int offset) {
        String sql = "SELECT * FROM task_t LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, rowMapper(), limit, offset);
    }

    private RowMapper<Task> rowMapper(){
        return (rs, rowNum) -> new Task(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                Status.valueOf((String) rs.getObject("status")),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
