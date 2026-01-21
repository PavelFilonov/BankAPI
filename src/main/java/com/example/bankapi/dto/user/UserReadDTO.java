package com.example.bankapi.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO пользователя")
@JsonInclude(NON_NULL)
public class UserReadDTO {

    @Schema(description = "Идентификатор пользователя")
    private Long id;

    @Schema(description = "Логин")
    private String login;

    @Schema(description = "Код активной роли")
    private String activeRoleCode;

    @Schema(description = "Значение активной роли")
    private String activeRoleValue;

    @Schema(description = "Роли пользователя")
    private List<RoleReadDTO> roles;

}
