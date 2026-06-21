package com.benevenuto.queue_master.infra.common.repository.jpa.interfaces;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @param <T> Tipo da Entidade de Domínio
 * @param <ID> Tipo do Identificador (UUID)
 * @param <R> Tipo da Interface JpaRepository (Spring Data)
 */
public abstract class BaseRepositoryOrderJpaImpl<T, ID, R extends JpaRepository<T, ID>> 
        extends BaseRepositoryJpaImpl<T, ID, R> {

    private final Class<T> entityClass;

    protected BaseRepositoryOrderJpaImpl(R repository, Class<T> entityClass) {
        super(repository);
        this.entityClass = entityClass;
    }

    // Os métodos save, findById, existsById, deleteById e findAll foram removidos 
    // daqui pois já são herdados diretamente de BaseRepositoryJpaImpl.

    public List<T> findByOperatorNumber(String operatorNumber) {
        try {
            T probe = entityClass.getDeclaredConstructor().newInstance();
            var field = entityClass.getDeclaredField("operatorNumber");
            field.setAccessible(true);
            field.set(probe, operatorNumber);
            
            return repository.findAll(Example.of(probe));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar por operatorNumber na base genérica", e);
        }
    }

    public List<T> findByWorkOrderNumber(String workOrderNumber) {
        try {
            T probe = entityClass.getDeclaredConstructor().newInstance();
            var field = entityClass.getDeclaredField("workOrderNumber");
            field.setAccessible(true);
            field.set(probe, workOrderNumber);
            
            return repository.findAll(Example.of(probe));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar por workOrderNumber na base genérica", e);
        }
    }
}