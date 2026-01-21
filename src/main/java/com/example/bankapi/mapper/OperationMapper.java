package com.example.bankapi.mapper;

import com.example.bankapi.dto.user.OperationReadDTO;
import com.example.bankapi.entity.auth.Operation;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface OperationMapper {

    OperationReadDTO toReadDto(Operation entity);

}
