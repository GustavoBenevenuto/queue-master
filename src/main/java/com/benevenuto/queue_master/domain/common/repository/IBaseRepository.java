package com.benevenuto.queue_master.domain.common.repository;

import java.util.List;
import java.util.Optional;

public interface IBaseRepository<T, ID> {
    T save(T t);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean existsById(ID id);
    void deleteById(ID id);
}