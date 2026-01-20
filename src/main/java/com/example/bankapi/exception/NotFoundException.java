package com.example.bankapi.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class NotFoundException extends BusinessException {

	public NotFoundException(String message) {
		super(message);
	}

}
