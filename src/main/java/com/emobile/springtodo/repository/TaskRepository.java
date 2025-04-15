package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM task ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Task> findAll(int limit, int offset);

}
