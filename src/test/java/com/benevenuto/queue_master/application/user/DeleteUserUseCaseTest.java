package com.benevenuto.queue_master.application.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.benevenuto.queue_master.domain.user.exceptions.UserNotFoundException;
import com.benevenuto.queue_master.domain.user.repository.IUserRepository;

class DeleteUserUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    private DeleteUserUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new DeleteUserUseCase(userRepository);
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).deleteById(id);
    }

    @Test
    void shouldDeleteWhenUserExists() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(true);

        useCase.execute(id);

        verify(userRepository).deleteById(id);
    }
}
