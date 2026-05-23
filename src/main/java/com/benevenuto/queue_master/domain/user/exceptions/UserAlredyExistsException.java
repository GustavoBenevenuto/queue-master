package com.benevenuto.queue_master.domain.user.exceptions;

import org.springframework.http.HttpStatus;

import com.benevenuto.queue_master.presentation.handleExceptions.exceptions.GlobalException;

import lombok.Data;

@Data
public class UserAlredyExistsException extends GlobalException {
	public UserAlredyExistsException() {
		super("The user alredy exists.", HttpStatus.BAD_REQUEST);
	}
}