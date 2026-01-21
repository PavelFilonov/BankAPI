package com.example.bankapi.service;

import com.example.bankapi.dto.card.CardCreateDTO;
import com.example.bankapi.dto.card.CardReadDTO;
import com.example.bankapi.dto.card.TransferDTO;
import com.example.bankapi.entity.Card;
import com.example.bankapi.entity.auth.User;
import com.example.bankapi.exception.BusinessException;
import com.example.bankapi.exception.NotFoundException;
import com.example.bankapi.mapper.CardMapper;
import com.example.bankapi.repository.CardRepository;
import com.example.bankapi.repository.UserRepository;
import com.example.bankapi.service.impl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.bankapi.enums.CardStatus.ACTIVE;
import static com.example.bankapi.enums.CardStatus.BLOCKED;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    private static final String USER_LOGIN = "test user";

    private CardService cardService;

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private UserRepository userRepository;

    private Card card;
    private CardReadDTO cardReadDTO;
    private User user;
    private LocalDate deactivationDate;
    private BigDecimal balance;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setLogin(USER_LOGIN);
        String number = "1111222233334444";
        deactivationDate = LocalDate.of(2027, 1, 22);
        balance = BigDecimal.valueOf(1000);
        card = new Card(number, user, deactivationDate, ACTIVE, balance);
        card.setId(1L);
        cardReadDTO = new CardReadDTO(1L, number, 1L, deactivationDate, ACTIVE, balance);

        lenient().when(cardRepository.save(card)).thenReturn(card);
        lenient().when(cardRepository.saveAndFlush(card)).thenReturn(card);
        lenient().when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        lenient().when(cardMapper.toReadDto(card)).thenReturn(cardReadDTO);

        cardService = new CardServiceImpl(cardRepository, cardMapper, userRepository);
    }

    @Test
    void create() {
        when(userRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.of(user));
        when(cardRepository.saveAndFlush(any(Card.class))).thenReturn(card);
        var createDTO = new CardCreateDTO(USER_LOGIN, deactivationDate, balance);

        var createdCard = cardService.create(createDTO);

        assertEquals(cardReadDTO, createdCard);
        verify(userRepository).findByLogin(USER_LOGIN);
        verify(cardRepository).saveAndFlush(any(Card.class));
        verify(cardMapper).toReadDto(any(Card.class));
    }

    @Test
    void getById_whenNotFound_thenThrowNotFoundException() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        var ex = assertThrows(NotFoundException.class, () -> cardService.getById(2L));

        assertTrue(ex.getMessage().contains("Карта по ИД=2 не найдена"));
        verify(cardRepository).findById(2L);
        verifyNoMoreInteractions(cardMapper);
    }

    @Test
    void getByUserLogin_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(List.of(card));
        when(cardRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(cardMapper.toReadDto(card)).thenReturn(cardReadDTO);

        Page<CardReadDTO> result = cardService.getByUserLogin(USER_LOGIN, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(cardReadDTO, result.getContent().getFirst());
        verify(cardRepository).findAll(any(Specification.class), eq(pageable));
        verify(cardMapper).toReadDto(card);
    }

    @Test
    void deleteById_whenNotFound_thenThrowNotFoundException() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        var ex = assertThrows(NotFoundException.class, () -> cardService.deleteById(2L));

        assertTrue(ex.getMessage().contains("Карта по ИД=2 не найдена"));
        verify(cardRepository).findById(2L);
        verify(cardRepository, never()).delete(any(Card.class));
    }

    @Test
    void blockById_whenNotFound_thenThrowNotFoundException() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        var ex = assertThrows(NotFoundException.class, () -> cardService.blockById(2L));

        assertTrue(ex.getMessage().contains("Карта по ИД=2 не найдена"));
        verify(cardRepository).findById(2L);
        verify(cardRepository, never()).save(any());
    }

    @Test
    void requestToBlockById_whenNotFound_thenThrowNotFoundException() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        var ex = assertThrows(NotFoundException.class, () -> cardService.requestToBlockById(2L));

        assertTrue(ex.getMessage().contains("Карта по ИД=2 не найдена"));
        verify(cardRepository).findById(2L);
        verify(cardRepository, never()).save(any());
    }

    @Test
    void activateById_whenNotFound_thenThrowNotFoundException() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        var ex = assertThrows(NotFoundException.class, () -> cardService.activateById(2L));

        assertTrue(ex.getMessage().contains("Карта по ИД=2 не найдена"));
        verify(cardRepository).findById(2L);
        verify(cardRepository, never()).save(any());
    }

    @Test
    void transfer_whenSameCard_thenThrowBusinessException() {
        var dto = new TransferDTO(1L, 1L, BigDecimal.valueOf(100));

        var ex = assertThrows(BusinessException.class, () -> cardService.transfer(dto));

        assertEquals("Нельзя переводить на ту же карту", ex.getMessage());
    }

    @Test
    void transfer_whenAmountNotPositive_thenThrowBusinessException() {
        var dtoZero = new TransferDTO(1L, 2L, BigDecimal.ZERO);
        var dtoNegative = new TransferDTO(1L, 2L, BigDecimal.valueOf(-10));

        assertThrows(BusinessException.class, () -> cardService.transfer(dtoZero));
        assertThrows(BusinessException.class, () -> cardService.transfer(dtoNegative));
    }

    @Test
    void transfer_whenSourceNotActive_thenThrowBusinessException() {
        User owner = new User();
        owner.setId(10L);
        owner.setLogin("owner");
        Card source = new Card("0000111122223333", owner, deactivationDate, BLOCKED, BigDecimal.valueOf(1000));
        source.setId(1L);
        Card target = new Card("4444333322221111", owner, deactivationDate, ACTIVE, BigDecimal.valueOf(1000));
        target.setId(2L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(source));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(target));
        var dto = new TransferDTO(1L, 2L, BigDecimal.valueOf(100));

        var ex = assertThrows(BusinessException.class, () -> cardService.transfer(dto));

        assertTrue(ex.getMessage().contains("Исходная карта ИД=1 не активна"));
    }

    @Test
    void transfer_whenTargetNotActive_thenThrowBusinessException() {
        User owner = new User();
        owner.setId(10L);
        owner.setLogin("owner");
        Card source = new Card("0000111122223333", owner, deactivationDate, ACTIVE, BigDecimal.valueOf(1000));
        source.setId(1L);
        Card target = new Card("4444333322221111", owner, deactivationDate, BLOCKED, BigDecimal.valueOf(1000));
        target.setId(2L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(source));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(target));
        var dto = new TransferDTO(1L, 2L, BigDecimal.valueOf(100));

        var ex = assertThrows(BusinessException.class, () -> cardService.transfer(dto));

        assertTrue(ex.getMessage().contains("Целевая карта ИД=2 не активна"));
    }

    @Test
    void transfer_whenSourceExpired_thenThrowBusinessException() {
        User owner = new User();
        owner.setId(10L);
        owner.setLogin("owner");
        Card source = new Card("0000111122223333", owner, now().minusDays(1), ACTIVE, BigDecimal.valueOf(1000));
        source.setId(1L);
        Card target = new Card("4444333322221111", owner, deactivationDate, ACTIVE, BigDecimal.valueOf(1000));
        target.setId(2L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(source));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(target));
        var dto = new TransferDTO(1L, 2L, BigDecimal.valueOf(100));

        var ex = assertThrows(BusinessException.class, () -> cardService.transfer(dto));

        assertTrue(ex.getMessage().contains("Исходная карта ИД=1 просрочена"));
    }

    @Test
    void transfer_whenTargetExpired_thenThrowBusinessException() {
        User owner = new User();
        owner.setId(10L);
        owner.setLogin("owner");
        Card source = new Card("0000111122223333", owner, deactivationDate, ACTIVE, BigDecimal.valueOf(1000));
        source.setId(1L);
        Card target = new Card("4444333322221111", owner, now().minusDays(1), ACTIVE, BigDecimal.valueOf(1000));
        target.setId(2L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(source));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(target));
        var dto = new TransferDTO(1L, 2L, BigDecimal.valueOf(100));

        var ex = assertThrows(BusinessException.class, () -> cardService.transfer(dto));

        assertTrue(ex.getMessage().contains("Целевая карта ИД=2 просрочена"));
    }

    @Test
    void transfer_whenInsufficientFunds_thenThrowBusinessException() {
        User owner = new User();
        owner.setId(10L);
        owner.setLogin("owner");
        Card source = new Card("0000111122223333", owner, deactivationDate, ACTIVE, BigDecimal.valueOf(50));
        source.setId(1L);
        Card target = new Card("4444333322221111", owner, deactivationDate, ACTIVE, BigDecimal.valueOf(1000));
        target.setId(2L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(source));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(target));
        var dto = new TransferDTO(1L, 2L, BigDecimal.valueOf(100));

        var ex = assertThrows(BusinessException.class, () -> cardService.transfer(dto));

        assertTrue(ex.getMessage().contains("Недостаточно средств на карте ИД=1"));
    }

    @Test
    void isOwnerOfCard_whenNotFound_thenThrowNotFoundException() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        var ex = assertThrows(NotFoundException.class, () -> cardService.isOwnerOfCard(USER_LOGIN, 2L));

        assertTrue(ex.getMessage().contains("Карта по ИД=2 не найдена"));
    }

    @Test
    void isOwnerOfCard_returnsFalseWhenDifferentOwner() {
        User other = new User();
        other.setId(5L);
        other.setLogin("other");
        Card otherCard = new Card("9999888877776666", other, deactivationDate, ACTIVE, BigDecimal.valueOf(100));
        otherCard.setId(2L);
        when(cardRepository.findById(2L)).thenReturn(Optional.of(otherCard));

        boolean result = cardService.isOwnerOfCard(USER_LOGIN, 2L);

        assertFalse(result);
    }

    @Test
    void isOwnerOfCard_returnsTrueWhenOwnerMatches() {
        boolean result = cardService.isOwnerOfCard(USER_LOGIN, 1L);

        assertTrue(result);
    }
}