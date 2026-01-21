package com.example.bankapi.repository;

import com.example.bankapi.entity.auth.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role> {

    Optional<Role> findByCode(String code);

}

