package com.benevenuto.queue_master.infra.user.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.domain.user.repositories.IUserRepository;
import com.benevenuto.queue_master.infra.common.repository.jpa.interfaces.BaseRepositoryJpaImpl;
import com.benevenuto.queue_master.infra.user.repositories.jpa.interfaces.IUserJpaRepository;

@Component
public class UserRepositoryJpaImpl extends BaseRepositoryJpaImpl<User, UUID, IUserJpaRepository> 
        implements IUserRepository {

    public UserRepositoryJpaImpl(IUserJpaRepository repository) {
        super(repository);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}