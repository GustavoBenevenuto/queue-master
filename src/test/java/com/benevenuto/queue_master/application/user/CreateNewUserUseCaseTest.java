package com.benevenuto.queue_master.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.benevenuto.queue_master.domain.user.constants.UserRole;
import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.domain.user.exceptions.UserAlredyExistsException;
import com.benevenuto.queue_master.domain.user.exceptions.UserNotAuthenticated;
import com.benevenuto.queue_master.domain.user.exceptions.UserWithoutRoleException;
import com.benevenuto.queue_master.domain.user.repository.IUserRepository;

class CreateNewUserUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CreateNewUserUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateNewUserUseCase(userRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowWhenNoAuthenticatedUser() {
        SecurityContextHolder.clearContext();

        User newUser = new User("Jane", "jane@mail.com", 1002, null, UserRole.OPERATOR, true);

        assertThatThrownBy(() -> useCase.execute(newUser))
                .isInstanceOf(UserNotAuthenticated.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenAuthenticatedUserIsNotAdmin() {
        authenticateAs("ROLE_OPERATOR");

        User newUser = new User("Jane", "jane@mail.com", 1002, null, UserRole.OPERATOR, true);

        assertThatThrownBy(() -> useCase.execute(newUser))
                .isInstanceOf(UserWithoutRoleException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        authenticateAs("ROLE_ADMIN");

        User newUser = new User("Jane", "jane@mail.com", 1002, null, UserRole.OPERATOR, true);
        when(userRepository.findByEmail("jane@mail.com")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> useCase.execute(newUser))
                .isInstanceOf(UserAlredyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldApplyDefaultPasswordWhenNoneIsProvided() {
        authenticateAs("ROLE_ADMIN");

        User newUser = new User("Jane", "jane@mail.com", 1002, null, UserRole.OPERATOR, true);
        when(userRepository.findByEmail("jane@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(CreateNewUserUseCase.DEFAULT_PASSWORD)).thenReturn("encoded-default");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User created = useCase.execute(newUser);

        assertThat(created.getPassword()).isEqualTo("encoded-default");
    }

    @Test
    void shouldEncryptProvidedPasswordInsteadOfDefault() {
        authenticateAs("ROLE_ADMIN");

        User newUser = new User("Jane", "jane@mail.com", 1002, "mySecret123", UserRole.OPERATOR, true);
        when(userRepository.findByEmail("jane@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("mySecret123")).thenReturn("encoded-custom");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User created = useCase.execute(newUser);

        assertThat(created.getPassword()).isEqualTo("encoded-custom");
    }

    private void authenticateAs(String authority) {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(authority));
        var authentication = new UsernamePasswordAuthenticationToken("user@mail.com", "n/a", authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
