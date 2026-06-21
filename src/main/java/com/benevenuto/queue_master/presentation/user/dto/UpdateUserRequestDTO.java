package com.benevenuto.queue_master.presentation.user.dto;

import com.benevenuto.queue_master.domain.user.constants.UserRole;

public record UpdateUserRequestDTO(
        String name,
        String email,
        Integer operatorNumber,
        UserRole role,
        Boolean active
) {}
