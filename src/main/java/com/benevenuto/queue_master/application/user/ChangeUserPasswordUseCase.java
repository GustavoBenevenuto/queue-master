package com.benevenuto.queue_master.application.user;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.domain.user.exceptions.InvalidCurrentPasswordException;
import com.benevenuto.queue_master.domain.user.exceptions.UserCannotChangeAnotherUserPasswordException;
import com.benevenuto.queue_master.domain.user.exceptions.UserNotAuthenticated;
import com.benevenuto.queue_master.domain.user.exceptions.UserNotFoundException;
import com.benevenuto.queue_master.domain.user.repository.IUserRepository;

public class ChangeUserPasswordUseCase {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangeUserPasswordUseCase(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(UUID userId, String currentPassword, String newPassword) {
        User authenticatedUser = getAuthenticatedUser();

        // Only the user itself can change its own password
        if (!authenticatedUser.getId().equals(userId)) {
            throw new UserCannotChangeAnotherUserPasswordException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCurrentPasswordException();
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            throw new UserNotAuthenticated();
        }

        return (User) authentication.getPrincipal();
    }
}
