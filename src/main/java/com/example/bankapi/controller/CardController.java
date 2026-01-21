package com.example.bankapi.controller;

import com.example.bankapi.dto.card.CardCreateDTO;
import com.example.bankapi.dto.card.CardReadDTO;
import com.example.bankapi.dto.card.TransferDTO;
import com.example.bankapi.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
@Tag(name = "Карты", description = "API для работы с картами")
public class CardController {

    private final CardService service;

    @Operation(summary = "Создание карты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Учётная запись успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Владелец не найден")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CARD_CREATE')")
    public ResponseEntity<CardReadDTO> create(
            @Parameter(description = "ДТО карты", required = true) @RequestBody @Valid CardCreateDTO dto
    ) {
        return ok(service.create(dto));
    }

    @Operation(summary = "Получение карты по ИД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта найдена"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CARD_READ') or (hasRole('USER') and @cardServiceImpl.isOwnerOfCard(principal.username, #id))")
    public ResponseEntity<CardReadDTO> getById(
            @Parameter(description = "Идентификатор карты", required = true)
            @PathVariable Long id
    ) {
        return ok(service.getById(id));
    }

    @Operation(summary = "Получение карты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список карт")
    })
    @GetMapping("/user/{userLogin}")
    @PreAuthorize("hasAuthority('CARD_LIST') or (hasRole('USER') and principal.username.equals(#userLogin))")
    public Page<CardReadDTO> getByUserLogin(
            @Parameter(description = "Логин пользователя", required = true)
            @PathVariable String userLogin,
            @PageableDefault(sort = "deactivationDate") Pageable pageable
    ) {
        return service.getByUserLogin(userLogin, pageable);
    }

    @Operation(summary = "Удаление карты по ИД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта удалена"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CARD_DELETE')")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Идентификатор карты", required = true)
            @PathVariable Long id
    ) {
        service.deleteById(id);
        return noContent().build();
    }

    @Operation(summary = "Блокирование карты по ИД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта заблокирована"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @PatchMapping("/{id}/block")
    @PreAuthorize("hasAuthority('CARD_BLOCK')")
    public ResponseEntity<CardReadDTO> blockById(
            @Parameter(description = "Идентификатор карты", required = true)
            @PathVariable Long id
    ) {
        return ok(service.blockById(id));
    }

    @Operation(summary = "Запрос на блокировку карты по ИД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрошена блокировка карты"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @PatchMapping("/{id}/request-to-block")
    @PreAuthorize("hasAuthority('CARD_REQUEST_BLOCK')")
    public ResponseEntity<CardReadDTO> requestToBlockById(
            @Parameter(description = "Идентификатор карты", required = true)
            @PathVariable Long id
    ) {
        return ok(service.requestToBlockById(id));
    }

    @Operation(summary = "Активация карты по ИД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта активирована"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('CARD_ACTIVATE')")
    public ResponseEntity<CardReadDTO> activateById(
            @Parameter(description = "Идентификатор карты", required = true)
            @PathVariable Long id
    ) {
        return ok(service.activateById(id));
    }

    @Operation(summary = "Перевод денег между картами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод завершён успешно"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Данные не найдены")
    })
    @PatchMapping("/transfer")
    @PreAuthorize("hasAuthority('CARD_TRANSFER') " +
            "and @cardServiceImpl.isOwnerOfCard(principal.username, #dto.sourceId) " +
            "and @cardServiceImpl.isOwnerOfCard(principal.username, #dto.targetId)")
    public ResponseEntity<Void> transfer(
            @Parameter(description = "ДТО перевода", required = true) @RequestBody @Valid TransferDTO dto
    ) {
        service.transfer(dto);
        return noContent().build();
    }

}
