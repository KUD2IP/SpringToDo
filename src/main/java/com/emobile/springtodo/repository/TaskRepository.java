package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Task;

import java.util.List;

public interface TaskRepository extends JdbcRepository<Long, Task> {

    List<Task> findAll(int limit, int offset);

}
