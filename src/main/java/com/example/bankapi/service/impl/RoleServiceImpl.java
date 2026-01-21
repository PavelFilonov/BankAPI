package com.example.bankapi.service.impl;

import com.example.bankapi.dto.user.RoleReadDTO;
import com.example.bankapi.mapper.RoleMapper;
import com.example.bankapi.repository.RoleRepository;
import com.example.bankapi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    private final RoleMapper mapper;

    @Override
    public List<RoleReadDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toReadDto)
                .toList();
    }

}
