package com.example.bankapi.service.impl;

import com.example.bankapi.dto.auth.AuthRequest;
import com.example.bankapi.dto.auth.AuthResponse;
import com.example.bankapi.dto.auth.RegisterRequest;
import com.example.bankapi.dto.user.UserReadDTO;
import com.example.bankapi.entity.auth.User;
import com.example.bankapi.exception.BusinessException;
import com.example.bankapi.exception.NotFoundException;
import com.example.bankapi.mapper.UserMapper;
import com.example.bankapi.repository.RoleRepository;
import com.example.bankapi.repository.UserRepository;
import com.example.bankapi.security.JwtProvider;
import com.example.bankapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE_CODE = "USER";

    private final AuthenticationManager authManager;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponse login(AuthRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.password())
        );
        String token = jwtProvider.generateToken(auth);
        return new AuthResponse(token);
    }

    @Override
    @Transactional
    public UserReadDTO register(RegisterRequest request) {
        var login = request.login();
        if (userRepository.findByLogin(login).isPresent()) {
            throw new BusinessException(format("Логин %s занят", login));
        }
        var entity = new User();
        entity.setLogin(login);
        entity.setPassword(passwordEncoder.encode(request.password()));
        var userRole = roleRepository.findByCode(DEFAULT_ROLE_CODE)
                .orElseThrow(() -> new NotFoundException("Роль по умолчанию отсутствует"));
        entity.setActiveRole(userRole);
        entity.setRoles(Set.of(userRole));
        return userMapper.toReadDto(userRepository.saveAndFlush(entity));
    }

}
