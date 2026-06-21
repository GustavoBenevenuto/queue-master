package com.benevenuto.queue_master.domain.user.exceptions;

import org.springframework.http.HttpStatus;

import com.benevenuto.queue_master.presentation.exception.GlobalException;

import lombok.Data;

@Data
public class UserNotAuthenticated extends GlobalException {
	public UserNotAuthenticated() {
		super("User is not authenticated.", HttpStatus.BAD_REQUEST);
	}
}