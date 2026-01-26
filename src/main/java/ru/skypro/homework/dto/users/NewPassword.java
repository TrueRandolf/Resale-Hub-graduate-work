package ru.skypro.homework.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Изменение пароля пользователя.
 * <p> Маппинг:
 * {@link ru.skypro.homework.mappers.UserMapper} </p>
 */

@Schema(description = "NewPassword")
@Data
public class NewPassword {

    @Schema(description = "текущий пароль",minLength = 8,maxLength = 16)
    @Size(min = 8, max = 16)
    private String currentPassword;

    @Schema(description = "новый пароль",minLength = 8,maxLength = 16)
    @Size(min = 8, max = 16)
    private String newPassword;
}
