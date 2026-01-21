package com.example.bankapi.service;

import com.example.bankapi.dto.user.OperationReadDTO;

import java.util.List;

public interface OperationService {

    /**
     * Получить все операции
     *
     * @return список операций
     */
    List<OperationReadDTO> getAll();

}
