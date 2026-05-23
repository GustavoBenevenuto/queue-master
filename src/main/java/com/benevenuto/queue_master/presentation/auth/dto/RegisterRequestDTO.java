package com.benevenuto.queue_master.presentation.auth.dto;

import com.benevenuto.queue_master.domain.user.constants.UserRole;

public record RegisterRequestDTO(String name, String email, Integer operatorNumber, String password, UserRole role) {

}
