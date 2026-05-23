package com.benevenuto.queue_master.presentation.handleExceptions.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class GlobalException extends RuntimeException {
	public HttpStatus status;
	
	public GlobalException(String message, HttpStatus status){
		super(message);
		this.status = status;
	}
}
