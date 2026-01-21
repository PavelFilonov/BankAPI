package com.example.bankapi.controller;

import com.example.bankapi.dto.user.OperationReadDTO;
import com.example.bankapi.dto.user.RoleReadDTO;
import com.example.bankapi.service.OperationService;
import com.example.bankapi.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/dictionary")
@RequiredArgsConstructor
@Tag(name = "Справочник", description = "API для работы со справочниками")
public class DictionaryController {

    private final RoleService roleService;

    private final OperationService operationService;

    @Operation(summary = "Получение списка ролей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список ролей")
    })
    @GetMapping("/role")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<List<RoleReadDTO>> getAllRoles() {
        return ok(roleService.getAll());
    }

    @Operation(summary = "Получение списка операций")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список операций")
    })
    @GetMapping("/operation")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<List<OperationReadDTO>> getAllOperations() {
        return ok(operationService.getAll());
    }

}
