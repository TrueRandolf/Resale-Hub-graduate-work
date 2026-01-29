package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;

/**
 * Сервис для управления профилями пользователей.
 *
 * <p>Реализует логику получения и обновления данных текущего пользователя и
 * смены пароля.</p>
 */

public interface UserService {

    /**
     * Смена пароля текущего авторизованного пользователя.
     * @param newPassword объект с текущим и новым паролем.
     * @param authentication данные пользователя из контекста безопасности.
     */
    void updateUserPassword(NewPassword newPassword, Authentication authentication);

    /** Получение данных профиля текущего пользователя. */
    User getAuthUserInfo(Authentication authentication);

    /**
     * Обновление персональных данных текущего пользователя.
     * @param updateUser новые данные (имя, фамилия, телефон).
     * @param authentication данные пользователя из контекста безопасности.
     */
    UpdateUser updateAuthUser(UpdateUser updateUser, Authentication authentication);

    /**
     * Обновление аватара текущего пользователя.
     * @param file загружаемый файл изображения.
     * @param authentication данные пользователя из контекста безопасности.
     */
    void updateAuthUserImage(MultipartFile file, Authentication authentication);

}
