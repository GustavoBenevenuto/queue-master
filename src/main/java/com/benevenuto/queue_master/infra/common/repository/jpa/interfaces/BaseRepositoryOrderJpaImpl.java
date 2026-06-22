package com.benevenuto.queue_master.infra.common.repository.jpa.interfaces;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
        return findByFieldEqual("operatorNumber", operatorNumber);
    }

    public List<T> findByWorkOrderNumber(String workOrderNumber) {
        return findByFieldEqual("workOrderNumber", workOrderNumber);
    }

    /**
     * Monta um Query by Example casando apenas o campo informado. Os demais campos são
     * explicitamente ignorados via ExampleMatcher, pois o QBE só ignora valores nulos por
     * padrão — campos com defaults não-nulos na declaração da entidade (ex.: isUrgent = false,
     * status = OrderStatus.pending) entrariam como filtro exato e quebrariam a busca.
     */
    private List<T> findByFieldEqual(String fieldName, Object value) {
        try {
            T probe = entityClass.getDeclaredConstructor().newInstance();
            Field field = entityClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(probe, value);

            String[] ignoredPaths = Arrays.stream(entityClass.getDeclaredFields())
                    .map(Field::getName)
                    .filter(name -> !name.equals(fieldName))
                    .toArray(String[]::new);

            ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths(ignoredPaths);

            return repository.findAll(Example.of(probe, matcher));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar por " + fieldName + " na base genérica", e);
        }
    }
}