package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.metric.BusinessMetric;
import ru.skypro.homework.service.ManagementService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление")
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/management/metric")
    @Operation(
            summary = "Вывод метрики пользователей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BusinessMetric.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            })
    public BusinessMetric getMetric(Authentication authentication) {
        return managementService.getBusinessMetric(authentication);
    }


    @DeleteMapping("/management/soft_delete_user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удаление пользователя с возможностью восстановления",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    public void softRemoveUser(@PathVariable Integer id, Authentication authentication) {
        managementService.softDeleteUser(Long.valueOf(id), authentication);
    }

    @DeleteMapping("/management/hard_delete_user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Полное удаление пользователя",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    public void hardRemoveUser(@PathVariable Integer id, Authentication authentication) {
        managementService.hardDeleteUser(Long.valueOf(id), authentication);
    }

}

