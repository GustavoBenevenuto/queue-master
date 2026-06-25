package com.benevenuto.queue_master.application.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.benevenuto.queue_master.domain.user.constants.UserRole;
import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.domain.user.exceptions.InvalidCurrentPasswordException;
import com.benevenuto.queue_master.domain.user.exceptions.UserCannotChangeAnotherUserPasswordException;
import com.benevenuto.queue_master.domain.user.exceptions.UserNotAuthenticated;
import com.benevenuto.queue_master.domain.user.exceptions.UserNotFoundException;
import com.benevenuto.queue_master.domain.user.repository.IUserRepository;

class ChangeUserPasswordUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ChangeUserPasswordUseCase useCase;

    private final UUID authenticatedUserId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ChangeUserPasswordUseCase(userRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowWhenNoAuthenticatedUser() {
        SecurityContextHolder.clearContext();

        assertThatThrownBy(() -> useCase.execute(UUID.randomUUID(), "old", "new"))
                .isInstanceOf(UserNotAuthenticated.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenTargetIdIsNotTheAuthenticatedUser() {
        authenticateAs(authenticatedUserId);
        UUID someoneElseId = UUID.randomUUID();

        assertThatThrownBy(() -> useCase.execute(someoneElseId, "old", "new"))
                .isInstanceOf(UserCannotChangeAnotherUserPasswordException.class);

        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        authenticateAs(authenticatedUserId);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(authenticatedUserId, "old", "new"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldThrowWhenCurrentPasswordDoesNotMatch() {
        authenticateAs(authenticatedUserId);
        User storedUser = new User("Jane", "jane@mail.com", 1002, "encoded-old", UserRole.OPERATOR, true);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(storedUser));
        when(passwordEncoder.matches("wrong-old", "encoded-old")).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(authenticatedUserId, "wrong-old", "new"))
                .isInstanceOf(InvalidCurrentPasswordException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUpdatePasswordWhenCurrentPasswordMatches() {
        authenticateAs(authenticatedUserId);
        User storedUser = new User("Jane", "jane@mail.com", 1002, "encoded-old", UserRole.OPERATOR, true);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(storedUser));
        when(passwordEncoder.matches("correct-old", "encoded-old")).thenReturn(true);
        when(passwordEncoder.encode("new-pass")).thenReturn("encoded-new");

        useCase.execute(authenticatedUserId, "correct-old", "new-pass");

        verify(userRepository).save(storedUser);
        org.assertj.core.api.Assertions.assertThat(storedUser.getPassword()).isEqualTo("encoded-new");
    }

    private void authenticateAs(UUID userId) {
        User principal = new User("Jane", "jane@mail.com", 1002, "encoded-old", UserRole.OPERATOR, true);
        principal.setId(userId);

        var authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
