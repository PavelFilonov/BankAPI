package com.example.bankapi.mapper;

import com.example.bankapi.dto.user.UserReadDTO;
import com.example.bankapi.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = {RoleMapper.class}, unmappedTargetPolicy = IGNORE)
public interface UserMapper {

    @Mapping(source = "activeRole.code", target = "activeRoleCode")
    @Mapping(source = "activeRole.description", target = "activeRoleValue")
    UserReadDTO toReadDto(User entity);

}
