package com.example.bankapi.dto.card;

import com.example.bankapi.enums.CardStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.example.bankapi.enums.CardStatus.ACTIVE;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO карты")
@JsonInclude(NON_NULL)
public class CardReadDTO {

    @Schema(description = "Идентификатор карты")
    private Long id;

    @Schema(description = "Номер")
    private String number;

    @Schema(description = "Идентификатор владельца")
    private Long ownerId;

    @Schema(description = "Дата деактивации")
    private LocalDate deactivationDate;

    @Schema(description = "Статус")
    private CardStatus status = ACTIVE;

    @Schema(description = "Баланс")
    private BigDecimal balance;

}
