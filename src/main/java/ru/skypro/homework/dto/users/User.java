package ru.skypro.homework.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ФОрма представления данных пользователя.
 * <p> Маппинг:
 * {@link ru.skypro.homework.mappers.UserMapper} </p>
 */

@Schema(description = "User")
@Data
public class User {

    @Schema(description = "id пользователя")
    private Integer id;

    @Schema(description = "логин пользователя")
    private String email;

    @Schema(description = "имя пользователя")
    private String firstName;

    @Schema(description = "фамилия пользователя")
    private String lastName;

    @Schema(description = "телефон пользователя")
    private String phone;

    @Schema(description = "роль пользователя")
    private String role;

    @Schema(description = "ссылка на аватар пользователя")
    private String image;


}
