package ru.skypro.homework.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Создание или редактирования данных пользователя.
 * <p> Маппинг:
 * {@link ru.skypro.homework.mappers.UserMapper} </p>
 */

@Schema(description = "UpdateUser")
@Data
public class UpdateUser {

    @Schema(description = "имя пользователя")
    @Size(min = 3, max = 10)
    private String firstName;

    @Schema(description = "фамилия пользователя")
    @Size(min = 3,max = 10)
    private String lastName;

    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}
