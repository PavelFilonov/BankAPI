package com.example.bankapi.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO операции")
@JsonInclude(NON_NULL)
public class OperationReadDTO {

    @Schema(description = "Идентификатор операции")
    private Long id;

    @Schema(description = "Код операции")
    private String code;

    @Schema(description = "Описание операции")
    private String description;

}
