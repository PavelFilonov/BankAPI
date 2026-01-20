package com.example.bankapi.exception;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@ResponseStatus(BAD_REQUEST)
public class BusinessException extends RuntimeException {

	private Object[] args;

	public BusinessException(final String message) {
		super(message);
	}

	public BusinessException(final String message, final Object... args) {
		super(message);
		this.args = args;
	}
}
