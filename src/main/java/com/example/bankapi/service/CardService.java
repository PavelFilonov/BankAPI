package com.example.bankapi.service;

import com.example.bankapi.dto.card.CardCreateDTO;
import com.example.bankapi.dto.card.CardReadDTO;
import com.example.bankapi.dto.card.TransferDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    /**
     * Создать карту
     *
     * @param dto ДТО создания карты
     * @return ДТО карты
     */
    CardReadDTO create(CardCreateDTO dto);

    /**
     * Получить карту по ИД
     *
     * @param id ИД карты
     * @return ДТО карты
     */
    CardReadDTO getById(Long id);

    /**
     * Получить карты пользователя
     *
     * @param userLogin логин пользователя
     * @param pageable  параметры пагинации
     * @return страница карт пользователя
     */
    Page<CardReadDTO> getByUserLogin(String userLogin, Pageable pageable);

    /**
     * Удалить карту
     *
     * @param id ИД удаляемой карты
     */
    void deleteById(Long id);

    /**
     * Заблокировать карту
     *
     * @param id ИД блокируемой карты
     * @return ДТО карты
     */
    CardReadDTO blockById(Long id);

    /**
     * Запросить блокировку карты
     *
     * @param id ИД блокируемой карты
     * @return ДТО карты
     */
    CardReadDTO requestToBlockById(Long id);

    /**
     * Активировать карту
     *
     * @param id ИД активируемой карты
     * @return ДТО карты
     */
    CardReadDTO activateById(Long id);

    /**
     * Перевод денег между картами
     *
     * @param transferDTO ДТО перевода
     */
    void transfer(TransferDTO transferDTO);

    /**
     * Является ли пользователь владельцем карты
     *
     * @param login  логин пользователя
     * @param cardId ИД карты
     */
    boolean isOwnerOfCard(String login, Long cardId);

}
