package com.benevenuto.queue_master.presentation.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.benevenuto.queue_master.application.user.ChangeUserPasswordUseCase;
import com.benevenuto.queue_master.application.user.CreateNewUserUseCase;
import com.benevenuto.queue_master.application.user.DeleteUserUseCase;
import com.benevenuto.queue_master.application.user.GetAllUsersUseCase;
import com.benevenuto.queue_master.application.user.UpdateUserUseCase;
import com.benevenuto.queue_master.domain.user.dto.UserDTO;
import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.presentation.user.dto.ChangePasswordRequestDTO;
import com.benevenuto.queue_master.presentation.user.dto.CreateUserRequestDTO;
import com.benevenuto.queue_master.presentation.user.dto.UpdateUserRequestDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateNewUserUseCase createNewUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final ChangeUserPasswordUseCase changeUserPasswordUseCase;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Valid CreateUserRequestDTO dto) {
        User newUser = new User(dto.name(), dto.email(), dto.operatorNumber(), null, dto.role(), true);
        User createdUser = createNewUserUseCase.execute(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(createdUser));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listAll() {
        List<UserDTO> users = getAllUsersUseCase.execute().stream().map(this::toDTO).toList();

        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable UUID id, @RequestBody UpdateUserRequestDTO dto) {
        User updatedUser = updateUserUseCase.execute(
                id, dto.name(), dto.email(), dto.operatorNumber(), dto.role(), dto.active());

        return ResponseEntity.ok(toDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUserUseCase.execute(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable UUID id, @RequestBody @Valid ChangePasswordRequestDTO dto) {
        changeUserPasswordUseCase.execute(id, dto.currentPassword(), dto.newPassword());

        return ResponseEntity.noContent().build();
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getOperatorNumber(),
                user.getRole(),
                user.getActive(),
                user.getLastLogin(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
