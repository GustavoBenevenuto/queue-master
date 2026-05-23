package com.benevenuto.queue_master.infra.user.repositories.jpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.domain.user.repositories.IUserRepository;
import com.benevenuto.queue_master.infra.user.repositories.jpa.interfaces.IUserJpaRepository;

@Component
public class UserRepositoryJpaImpl implements IUserRepository {

    private final IUserJpaRepository repository;

    public UserRepositoryJpaImpl(IUserJpaRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}