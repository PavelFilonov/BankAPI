package com.example.bankapi.repository;

import com.example.bankapi.entity.base.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, UUID>, JpaSpecificationExecutor<E> {

}
