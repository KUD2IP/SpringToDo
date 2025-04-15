package com.emobile.springtodo.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public class AbstractTestContainers {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("task_db")
            .withUsername("testusername")
            .withPassword("testpassword");

    protected static DataSource dataSource;
    protected static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setup() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(postgres.getJdbcUrl());
        ds.setUsername(postgres.getUsername());
        ds.setPassword(postgres.getPassword());
        ds.setMaximumPoolSize(5);
        dataSource = ds;
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.execute("""
            DROP TABLE IF EXISTS task;
            CREATE TABLE task (
                id SERIAL PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                description TEXT,
                status VARCHAR(255),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """);
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM task");
        jdbcTemplate.execute("ALTER SEQUENCE task_id_seq RESTART WITH 1");
    }
}
