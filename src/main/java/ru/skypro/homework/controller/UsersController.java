package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;

/**
 * Контроллер для работы с информацией о пользователе.
 * Обеспечивает CRUD-операции и управление информацией и паролем пользователя.
 * <p>Доступ к методам редактирования разрешен для:</p>
 * <ul>
 *     <li> Автора объявления</li>
 *     <li> Админа {@link ru.skypro.homework.dto.Role#ADMIN}  </li>
 * </ul>
 *
 * @see ru.skypro.homework.service.UserService
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UsersController {

    private final UserService userService;

    @PostMapping("/users/set_password")
    @Operation(
            summary = "Обновление пароля",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            }
    )
    public void setPassword(@Valid @RequestBody NewPassword newPassword, Authentication authentication) {
        userService.updateUserPassword(newPassword, authentication);
    }

    @GetMapping("/users/me")
    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public User getUser(Authentication authentication) {
        return userService.getAuthUserInfo(authentication);
    }

    @PatchMapping("/users/me")
    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUser.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public UpdateUser updateUser(@Valid @RequestBody(required = false) UpdateUser user, Authentication authentication) {
        return userService.updateAuthUser(user, authentication);
    }

    @PatchMapping(value = "/users/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public void updateUserImage(@RequestPart("image") MultipartFile image, Authentication authentication) {
        userService.updateAuthUserImage(image, authentication);
    }

}
