package com.example.bankapi.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorResponse {
	private LocalDateTime timestamp;
	private HttpStatus status;
	private int code;
	private String message;
	private String path;
	private Map<String, Object> params;
}
