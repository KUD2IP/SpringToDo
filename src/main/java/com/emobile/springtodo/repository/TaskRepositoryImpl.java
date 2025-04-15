package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final SessionFactory sessionFactory;

    public TaskRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Task task) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Long generateId = session.merge(task).getId();
        log.info("Saving task: {}", task);

        session.getTransaction().commit();
        session.close();

        task.setId(generateId);
    }

    @Override
    public Optional<Task> findById(Long id) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Task task = session.get(Task.class, id);
        log.info("Found task: {}", task);

        session.getTransaction().commit();
        session.close();

        return Optional.ofNullable(task);
    }

    @Override
    public List<Task> findAll() {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Task> tasks = session.createQuery("from Task", Task.class).stream().toList();
        log.info("Found {} tasks", tasks.size());

        session.getTransaction().commit();
        session.close();

        return tasks;
    }

    @Override
    public void deleteById(Long id) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Task task = session.get(Task.class, id);
        if (task != null) {
            session.remove(task);
            log.info("Deleted task: {}", task);
        }

        session.getTransaction().commit();
        session.close();

    }

    @Override
    public void update(Task task) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.merge(task);
        log.info("Updating task: {}", task);

        session.getTransaction().commit();
        session.close();

    }

    @Override
    public List<Task> findAll(int limit, int offset) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Task> tasks = session.createQuery("from Task", Task.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();

        log.info("Found {} tasks", tasks.size());

        session.getTransaction().commit();
        session.close();

        return tasks;
    }
}
