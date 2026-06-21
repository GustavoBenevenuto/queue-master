package com.benevenuto.queue_master.infra.user.repository.jpa.interfaces;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.benevenuto.queue_master.domain.user.entity.User;

public interface IUserJpaRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}