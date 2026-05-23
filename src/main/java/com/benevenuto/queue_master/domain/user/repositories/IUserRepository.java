package com.benevenuto.queue_master.domain.user.repositories;

import java.util.Optional;
import java.util.UUID;

import com.benevenuto.queue_master.domain.order_queue.repository.IBaseRepository;
import com.benevenuto.queue_master.domain.user.entity.User;

public interface IUserRepository extends IBaseRepository<User, UUID> {
	Optional<User> findByEmail(String email);
}