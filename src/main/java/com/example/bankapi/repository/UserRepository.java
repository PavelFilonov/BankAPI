package com.example.bankapi.repository;

import com.example.bankapi.entity.auth.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User> {

}

