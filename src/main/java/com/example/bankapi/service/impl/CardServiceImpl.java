package com.example.bankapi.service.impl;

import com.example.bankapi.dto.CardCreateDTO;
import com.example.bankapi.dto.CardReadDTO;
import com.example.bankapi.dto.TransferDTO;
import com.example.bankapi.entity.Card;
import com.example.bankapi.exception.BusinessException;
import com.example.bankapi.exception.NotFoundException;
import com.example.bankapi.mapper.CardMapper;
import com.example.bankapi.repository.CardRepository;
import com.example.bankapi.repository.UserRepository;
import com.example.bankapi.service.CardService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.example.bankapi.enums.CardStatus.*;
import static com.example.bankapi.util.CardNumberGenerator.generateRandomCardNumber;
import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static java.time.LocalDate.now;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    // Карта локов для каждой карты
    private final ConcurrentMap<Long, Object> LOCKS = new ConcurrentHashMap<>();

    private final CardRepository repository;
    private final CardMapper mapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CardReadDTO create(@NonNull CardCreateDTO dto) {
        var entity = new Card();
        entity.setNumber(generateRandomCardNumber());
        entity.setDeactivationDate(dto.getDeactivationDate());
        entity.setStatus(ACTIVE);
        entity.setBalance(dto.getBalance());

        Long ownerId = dto.getOwnerId();
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(format("Владелец по ИД=%s не найден", ownerId)));
        entity.setOwner(owner);

        return mapper.toReadDto(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public CardReadDTO getById(Long id) {
        return mapper.toReadDto(getEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardReadDTO> getByUserId(Long userId, Pageable pageable) {
        return repository.findAll(CardRepository.byOwnerIdSpec(userId), pageable)
                .map(mapper::toReadDto);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        var entity = getEntityById(id);
        repository.delete(entity);
    }

    @Override
    @Transactional
    public CardReadDTO blockById(Long id) {
        var entity = getEntityById(id);
        entity.setStatus(BLOCKED);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    @Transactional
    public CardReadDTO requestToBlockById(Long id) {
        var entity = getEntityById(id);
        entity.setStatus(REQUEST_TO_BLOCK);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    @Transactional
    public CardReadDTO activateById(Long id) {
        var entity = getEntityById(id);
        entity.setStatus(ACTIVE);
        return mapper.toReadDto(repository.save(entity));
    }

    // Метод содержит синхронизацию объектов по картам + оптимистическая блокировка с version
    @Override
    @Transactional
    public void transfer(@NonNull TransferDTO transferDTO) {
        Long sourceId = transferDTO.getSourceId();
        Long targetId = transferDTO.getTargetId();
        var amount = transferDTO.getAmount();
        if (sourceId.equals(targetId)) {
            throw new BusinessException("Нельзя переводить на ту же карту");
        }
        if (amount == null || amount.compareTo(ZERO) <= 0) {
            throw new BusinessException("Сумма перевода должна быть положительной");
        }
        Long firstId = sourceId < targetId
                ? sourceId
                : targetId;
        Long secondId = sourceId < targetId
                ? targetId
                : sourceId;
        Object firstLock = getLock(firstId);
        Object secondLock = getLock(secondId);

        synchronized (firstLock) {
            synchronized (secondLock) {
                var source = getEntityById(sourceId);
                var target = getEntityById(targetId);

                if (source.getStatus() != ACTIVE) {
                    throw new BusinessException(format("Исходная карта ИД=%s не активна", sourceId));
                }
                if (target.getStatus() != ACTIVE) {
                    throw new BusinessException(format("Целевая карта ИД=%s не активна", targetId));
                }
                if (source.getDeactivationDate().isBefore(now())) {
                    throw new BusinessException(format("Исходная карта ИД=%s просрочена", sourceId));
                }
                if (target.getDeactivationDate().isBefore(now())) {
                    throw new BusinessException(format("Целевая карта ИД=%s просрочена", targetId));
                }
                if (source.getBalance().compareTo(amount) < 0) {
                    throw new BusinessException(format("Недостаточно средств на карте ИД=%s", sourceId));
                }

                source.setBalance(source.getBalance().subtract(amount));
                target.setBalance(target.getBalance().add(amount));
                repository.saveAndFlush(source);
                repository.saveAndFlush(target);
            }
        }
        LOCKS.computeIfPresent(firstId, (k, v) -> v == firstLock ? null : v);
        LOCKS.computeIfPresent(secondId, (k, v) -> v == secondLock ? null : v);
    }

    private Card getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("Карта по ИД=%s не найдена", id)));
    }

    private Object getLock(Long cardId) {
        return LOCKS.computeIfAbsent(cardId, id -> new Object());
    }

}
