package com.example.bankapi.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardStatus {

	ACTIVE("Активна"),
	BLOCKED("Заблокирована"),
	EXPIRED("Истёк срок");

	@JsonValue
	private final String value;

}
