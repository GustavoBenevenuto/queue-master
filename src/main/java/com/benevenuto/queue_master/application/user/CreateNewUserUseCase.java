package com.benevenuto.queue_master.application.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.benevenuto.queue_master.domain.user.constants.UserRole;
import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.domain.user.exceptions.UserAlredyExistsException;
import com.benevenuto.queue_master.domain.user.exceptions.UserNotAuthenticated;
import com.benevenuto.queue_master.domain.user.exceptions.UserWithoutRoleException;
import com.benevenuto.queue_master.domain.user.repositories.IUserRepository;

public class CreateNewUserUseCase {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateNewUserUseCase(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(User newUser) {
        // 1. Authorization Check: Only ADMIN can register new users
        checkAdminPrivileges();

        // 2. Uniqueness Check
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new UserAlredyExistsException();
        }

        // 3. Password Encryption
        String encryptedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encryptedPassword);

        // 4. Persistence
        return userRepository.save(newUser);
    }

    /**
     * Checks if the currently authenticated user has ADMIN privileges.
     */
    private void checkAdminPrivileges() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthenticated();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ADMIN.getRoleName()));

        if (!isAdmin) {
            throw new UserWithoutRoleException();
        }
    }
}