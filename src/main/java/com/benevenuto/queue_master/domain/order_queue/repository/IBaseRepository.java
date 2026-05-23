package com.benevenuto.queue_master.domain.order_queue.repository;

import java.util.Optional;

public interface IBaseRepository<T, ID> {
	T save(T t);
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    void deleteById(ID id);
}
