package com.benevenuto.queue_master.application.user;

import java.util.UUID;

import com.benevenuto.queue_master.domain.user.constants.UserRole;
import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.domain.user.exceptions.UserNotFoundException;
import com.benevenuto.queue_master.domain.user.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final IUserRepository userRepository;

    public User execute(UUID id, String name, String email, Integer operatorNumber, UserRole role, Boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (name != null) {
            user.setName(name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (operatorNumber != null) {
            user.setOperatorNumber(operatorNumber);
        }
        if (role != null) {
            user.setRole(role);
        }
        if (active != null) {
            user.setActive(active);
        }

        return userRepository.save(user);
    }
}
