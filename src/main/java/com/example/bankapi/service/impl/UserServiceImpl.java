package com.example.bankapi.service.impl;

import com.example.bankapi.entity.auth.User;
import com.example.bankapi.exception.NotFoundException;
import com.example.bankapi.repository.UserRepository;
import com.example.bankapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private User getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("Пользователь по id=%s не найден", id)));
    }

}
