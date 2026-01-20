package com.example.bankapi.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * Исключение конфликта при сохранении сущности.
 */
@ResponseStatus(CONFLICT)
public class UpdateConflictException extends BusinessException {
	public UpdateConflictException(String message) {
		super(message);
	}

}
