package com.example.bankapi.mapper;

import com.example.bankapi.dto.user.RoleReadDTO;
import com.example.bankapi.entity.auth.Role;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface RoleMapper {

    RoleReadDTO toReadDto(Role entity);

}
