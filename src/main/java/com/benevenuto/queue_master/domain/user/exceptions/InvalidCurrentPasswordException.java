package com.benevenuto.queue_master.domain.user.exceptions;

import org.springframework.http.HttpStatus;

import com.benevenuto.queue_master.presentation.exception.GlobalException;

import lombok.Data;

@Data
public class InvalidCurrentPasswordException extends GlobalException {
	public InvalidCurrentPasswordException() {
		super("Current password is incorrect.", HttpStatus.BAD_REQUEST);
	}
}
