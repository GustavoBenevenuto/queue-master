package com.benevenuto.queue_master.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import com.benevenuto.queue_master.domain.common.repository.IBaseRepository;
import com.benevenuto.queue_master.domain.user.entity.User;

public interface IUserRepository extends IBaseRepository<User, UUID> {
	Optional<User> findByEmail(String email);
}