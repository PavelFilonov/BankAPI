package com.example.bankapi.service.impl;

import com.example.bankapi.dto.user.OperationReadDTO;
import com.example.bankapi.mapper.OperationMapper;
import com.example.bankapi.repository.OperationRepository;
import com.example.bankapi.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final OperationRepository repository;

    private final OperationMapper mapper;

    @Override
    public List<OperationReadDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toReadDto)
                .toList();
    }

}
