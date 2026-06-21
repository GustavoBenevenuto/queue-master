package com.benevenuto.queue_master.application.user;

import java.util.List;

import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.domain.user.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllUsersUseCase {

    private final IUserRepository userRepository;

    public List<User> execute() {
        return userRepository.findAll();
    }
}
