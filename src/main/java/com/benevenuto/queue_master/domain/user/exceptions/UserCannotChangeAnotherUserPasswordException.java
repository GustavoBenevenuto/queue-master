package com.benevenuto.queue_master.domain.user.exceptions;

import org.springframework.http.HttpStatus;

import com.benevenuto.queue_master.presentation.exception.GlobalException;

import lombok.Data;

@Data
public class UserCannotChangeAnotherUserPasswordException extends GlobalException {
	public UserCannotChangeAnotherUserPasswordException() {
		super("You can only change your own password.", HttpStatus.FORBIDDEN);
	}
}
