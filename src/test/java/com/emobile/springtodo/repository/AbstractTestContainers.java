package com.emobile.springtodo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class AbstractTestContainers {

    @Container
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("task_db")
            .withUsername("testusername")
            .withPassword("testpassword");

    protected static SessionFactory sessionFactory;

    @BeforeAll
    static void setup() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.url", postgres.getJdbcUrl())
                .applySetting("hibernate.connection.username", postgres.getUsername())
                .applySetting("hibernate.connection.password", postgres.getPassword())
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .applySetting("hibernate.hbm2ddl.auto", "create-drop")
                .applySetting("hibernate.show_sql", "true")
                .applySetting("hibernate.format_sql", "true")
                .build();

        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(com.emobile.springtodo.model.entity.Task.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    @AfterEach
    void cleanup() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.doWork(connection -> {
                try (var statement = connection.createStatement()) {
                    statement.execute("TRUNCATE TABLE task RESTART IDENTITY CASCADE");
                }
            });
            session.getTransaction().commit();
        }
    }

    protected Session openSession() {
        return sessionFactory.openSession();
    }
}
