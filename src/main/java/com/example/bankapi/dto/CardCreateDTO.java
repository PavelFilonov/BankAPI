package com.example.bankapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO для создания карты")
public class CardCreateDTO {

    @Schema(description = "Идентификатор владельца")
    @NotNull(message = "ИД владельца обязателен к заполнению")
    private Long ownerId;

    @Schema(description = "Дата деактивации")
    @NotNull(message = "Дата деактивации обязательна к заполнению")
    private LocalDate deactivationDate;

    @Schema(description = "Баланс")
    @NotNull(message = "Баланс обязателен к заполнению")
    private BigDecimal balance;

}
