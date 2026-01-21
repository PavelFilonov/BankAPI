package com.example.bankapi.controller;

import com.example.bankapi.dto.auth.AuthRequest;
import com.example.bankapi.dto.auth.AuthResponse;
import com.example.bankapi.dto.auth.RegisterRequest;
import com.example.bankapi.dto.user.UserReadDTO;
import com.example.bankapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Логин и регистрация", description = "API для логина и регистрации")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Логин пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь авторизован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "ДТО запроса логина", required = true)
            @RequestBody @Valid AuthRequest request
    ) {
        return ok(authService.login(request));
    }

    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Логин занят"),
            @ApiResponse(responseCode = "404", description = "Роль по умолчанию не найдена")
    })
    @PostMapping("/register")
    public ResponseEntity<UserReadDTO> register(
            @Parameter(description = "ДТО запроса регистрации", required = true)
            @RequestBody @Valid RegisterRequest request
    ) {
        return ok(authService.register(request));
    }

}

