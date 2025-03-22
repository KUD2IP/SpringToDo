package com.emobile.springtodo.repository;

import java.util.List;
import java.util.Optional;

public interface JdbcRepository<T, R> {

    void save(R task);

    Optional<R> findById(T id);

    List<R> findAll();

    void deleteById(T id);

    void update(R task);
}
