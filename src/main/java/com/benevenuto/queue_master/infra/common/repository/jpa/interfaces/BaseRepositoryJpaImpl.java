package com.benevenuto.queue_master.infra.order_queue.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @param <T> Tipo da Entidade de Domínio
 * @param <ID> Tipo do Identificador (UUID)
 * @param <R> Tipo da Interface JpaRepository (Spring Data)
 */
public abstract class BaseRepositoryJpaImpl<T, ID, R extends JpaRepository<T, ID>> {

    protected final R repository;

    protected BaseRepositoryJpaImpl(R repository) {
        this.repository = repository;
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}