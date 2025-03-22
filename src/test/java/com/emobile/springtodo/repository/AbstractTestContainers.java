package com.emobile.springtodo.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public class AbstractTestContainers {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("task_db")
            .withUsername("testusername")
            .withPassword("testpassword");

    protected static DataSource dataSource;

    @BeforeAll
    static void setup() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(postgres.getJdbcUrl());
        ds.setUsername(postgres.getUsername());
        ds.setPassword(postgres.getPassword());
        dataSource = ds;

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS task_t ("
                        + "id SERIAL PRIMARY KEY, "
                        + "title VARCHAR(255) NOT NULL, "
                        + "description TEXT, "
                        + "status VARCHAR(255) NOT NULL, "
                        + "created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                        + ")");
    }

    @BeforeEach
    void cleanup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE TABLE task_t RESTART IDENTITY CASCADE");
    }
}
