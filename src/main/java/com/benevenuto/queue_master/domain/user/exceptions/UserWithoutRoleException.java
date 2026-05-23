package com.benevenuto.queue_master.domain.user.exceptions;

import org.springframework.http.HttpStatus;

import com.benevenuto.queue_master.presentation.handleExceptions.exceptions.GlobalException;

import lombok.Data;

@Data
public class UserWithoutRoleException extends GlobalException {
	public HttpStatus status;

	public UserWithoutRoleException() {
		super("The user doesn’t have a role.", HttpStatus.FORBIDDEN);
	}
}