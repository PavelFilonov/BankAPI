package com.example.bankapi.controller;

import com.example.bankapi.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAnyException(Exception ex, HttpServletRequest request) {
		var status = resolveStatus(ex);

		var response = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(status)
				.code(status.value())
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.status(status).body(response);
	}

	private HttpStatus resolveStatus(Exception ex) {
		ResponseStatus status = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
		return status != null ? status.value() : HttpStatus.INTERNAL_SERVER_ERROR;
	}
}