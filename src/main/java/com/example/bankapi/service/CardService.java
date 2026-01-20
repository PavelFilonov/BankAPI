package com.example.bankapi.service;

import com.example.bankapi.dto.CardCreateDTO;
import com.example.bankapi.dto.CardReadDTO;
import com.example.bankapi.dto.TransferDTO;
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
     * @param userId   ИД пользователя
     * @param pageable параметры пагинации
     * @return страница карт пользователя
     */
    Page<CardReadDTO> getByUserId(Long userId, Pageable pageable);

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

}
