package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;

/**
 * Сервис для управления профилями пользователей.
 *
 * <p>Реализует логику получения и обновления данных текущего пользователя,
 * смены пароля, а также механизмы мягкого и полного удаления аккаунтов.</p>
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

    /**
     * Служебный метод (soft-delete).
     * <p>Мягкое удаление пользователя:</p>
     * <ul>
     * <li>Изменение учетных данных в БД (анонимизация)</li>
     * <li>Удаление объявлений из БД.</li>
     * <li>Удаление медиа-контента с диска.</li>
     * </ul>
     * @param id идентификатор пользователя.
     * @param authentication данные пользователя из контекста безопасности.
     */
    void softDeleteUser(Long id, Authentication authentication);

    /**
     * Служебный метод (hard-delete).
     * Жесткое удаление пользователя:
     * <ul>
     * <li>Полное удаление пользователя и всех связанных данных из БД.</li>
     * <li>Удаление медиа-контента с диска.</li>
     * </ul>
     * @param id идентификатор пользователя
     * @param authentication данные пользователя из контекста безопасности.
     */
    void hardDeleteUser(Long id, Authentication authentication);
}
