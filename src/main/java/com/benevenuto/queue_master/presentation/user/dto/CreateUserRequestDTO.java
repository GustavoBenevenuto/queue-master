package com.benevenuto.queue_master.presentation.user.dto;

import com.benevenuto.queue_master.domain.user.constants.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequestDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull Integer operatorNumber,
        @NotNull UserRole role
) {}
