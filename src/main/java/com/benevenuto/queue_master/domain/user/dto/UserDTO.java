package com.benevenuto.queue_master.domain.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.benevenuto.queue_master.domain.user.constants.UserRole;

public record UserDTO (
		UUID id, 
		String name, 
		String email, 
		Integer operatorNumber, 
		UserRole role, 
		Boolean active,
		LocalDateTime lastLogin,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
) {}