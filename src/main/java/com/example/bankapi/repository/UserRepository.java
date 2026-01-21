package com.example.bankapi.repository;

import com.example.bankapi.entity.auth.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByLogin(String login);

}

