package com.benevenuto.queue_master.domain.user.exceptions;

import org.springframework.http.HttpStatus;

import com.benevenuto.queue_master.presentation.exception.GlobalException;

import lombok.Data;

@Data
public class UserNotFoundException extends GlobalException {
	public UserNotFoundException() {
		super("User not found.", HttpStatus.NOT_FOUND);
	}
}
