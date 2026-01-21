package com.example.bankapi.service;

import com.example.bankapi.dto.auth.AuthRequest;
import com.example.bankapi.dto.auth.AuthResponse;
import com.example.bankapi.dto.auth.RegisterRequest;
import com.example.bankapi.dto.user.UserReadDTO;

public interface AuthService {

    /**
     * Логин пользователя
     *
     * @param request ДТО запроса логина
     * @return ДТО ответа логина
     */
    AuthResponse login(AuthRequest request);

    /**
     * Регистрация пользователя
     *
     * @param request ДТО запроса регистрации
     * @return ДТО ответа регистрации
     */
    UserReadDTO register(RegisterRequest request);

}
