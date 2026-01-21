package com.example.bankapi.service;

import com.example.bankapi.dto.user.RoleReadDTO;

import java.util.List;

public interface RoleService {

    /**
     * Получить все роли
     *
     * @return список ролей
     */
    List<RoleReadDTO> getAll();

}
