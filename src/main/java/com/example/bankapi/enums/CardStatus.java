package com.example.bankapi.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardStatus {

    ACTIVE("Активна"),
    BLOCKED("Заблокирована"),
    REQUEST_TO_BLOCK("Запрошена блокировка"), // Введён статус для промежуточного состояния в ожидании блокировки
    EXPIRED("Истёк срок");

    @JsonValue
    private final String value;

}
