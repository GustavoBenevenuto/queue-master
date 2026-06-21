package com.benevenuto.queue_master.application.user;

import java.util.UUID;

import com.benevenuto.queue_master.domain.user.exceptions.UserNotFoundException;
import com.benevenuto.queue_master.domain.user.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final IUserRepository userRepository;

    public void execute(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }
}
