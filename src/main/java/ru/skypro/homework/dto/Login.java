package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Данные для аутентификации пользователя.
 *
 * <p>Передается в теле запроса при входе в систему.</p>
 * {@link ru.skypro.homework.controller.AuthController}
 */

@Data
@Schema(description = "Login")
public class Login {

    @Schema(description = "логин", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16)
    private String username;

    @Schema(description = "пароль", minLength = 4, maxLength = 32)
    @Size(min = 4, max = 32)
    private String password;
}
