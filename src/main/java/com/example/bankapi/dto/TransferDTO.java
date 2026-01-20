package com.example.bankapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO для перевода денег")
public class TransferDTO {

    @Schema(description = "Идентификатор карты откуда переводим")
    @NotNull(message = "ИД карты откуда переводим обязателен к заполнению")
    private Long sourceId;

    @Schema(description = "Идентификатор карты куда переводим")
    @NotNull(message = "ИД карты куда переводим обязателен к заполнению")
    private Long targetId;

    @Schema(description = "Количество")
    @NotNull(message = "Количество обязательно к заполнению")
    private BigDecimal amount;

}
