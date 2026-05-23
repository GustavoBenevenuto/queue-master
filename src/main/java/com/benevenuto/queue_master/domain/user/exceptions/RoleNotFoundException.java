package com.benevenuto.queue_master.domain.user.exceptions;

import org.springframework.http.HttpStatus;

import com.benevenuto.queue_master.presentation.handleExceptions.exceptions.GlobalException;

import lombok.Data;

@Data
public class RoleNotFoundException extends GlobalException {
	public RoleNotFoundException() {
		super("This role does not exist.", HttpStatus.BAD_REQUEST);
	}
}