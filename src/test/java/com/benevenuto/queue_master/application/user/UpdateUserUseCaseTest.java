package com.benevenuto.queue_master.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.benevenuto.queue_master.domain.user.constants.UserRole;
import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.domain.user.exceptions.UserNotFoundException;
import com.benevenuto.queue_master.domain.user.repository.IUserRepository;

class UpdateUserUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    private UpdateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new UpdateUserUseCase(userRepository);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id, "name", "email", 1, UserRole.ADMIN, true))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldOnlyUpdateFieldsThatWereProvided() {
        UUID id = UUID.randomUUID();
        User existing = new User("Old Name", "old@mail.com", 1001, "encoded", UserRole.OPERATOR, true);
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Only "name" and "active" are provided; the rest stay untouched.
        User updated = useCase.execute(id, "New Name", null, null, null, false);

        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getEmail()).isEqualTo("old@mail.com");
        assertThat(updated.getOperatorNumber()).isEqualTo(1001);
        assertThat(updated.getRole()).isEqualTo(UserRole.OPERATOR);
        assertThat(updated.getActive()).isFalse();
    }

    @Test
    void shouldUpdateAllFieldsWhenAllAreProvided() {
        UUID id = UUID.randomUUID();
        User existing = new User("Old Name", "old@mail.com", 1001, "encoded", UserRole.OPERATOR, true);
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = useCase.execute(id, "New Name", "new@mail.com", 2002, UserRole.INVENTOR, false);

        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getEmail()).isEqualTo("new@mail.com");
        assertThat(updated.getOperatorNumber()).isEqualTo(2002);
        assertThat(updated.getRole()).isEqualTo(UserRole.INVENTOR);
        assertThat(updated.getActive()).isFalse();
    }
}
