package com.sapient.moviebooking.service;

import java.util.Collection;
import java.util.Optional;

public interface CrudService<T, K> {
    T save(T entity);

    Optional<T> findById(K id);

    boolean existsById(K id);

    Collection<T> findAll();
}
