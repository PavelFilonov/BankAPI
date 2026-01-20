package com.example.bankapi.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class CardNumberGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // TODO: переделать в зависимости от требований генерации
    public static String generateRandomCardNumber() {
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(SECURE_RANDOM.nextInt(10));
        }
        return sb.toString();
    }

}
