package com.example.bankapi.repository;

import com.example.bankapi.entity.auth.Operation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationRepository extends BaseRepository<Operation> {

    Optional<Operation> findByCode(String code);

}

