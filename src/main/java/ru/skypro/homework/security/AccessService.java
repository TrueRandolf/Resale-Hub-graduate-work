package ru.skypro.homework.security;

import org.springframework.security.core.Authentication;

/**
 * Сервис проверки прав доступа и авторизации.
 *
 * <p>Обеспечивает централизованную проверку полномочий пользователя
 * перед выполнением операций в контроллерах.</p>
 */

public interface AccessService {

    /**Проверка аутентификации пользователя в системе */
    void checkAuth(Authentication authentication);

    /**Проверка права на редактирование контента.
     * <p>Нужны права автора контента или правав админстратора </p>
     * <p>{@link ru.skypro.homework.dto.Role#ADMIN}<p/> */
    void checkEdit(Authentication authentication, String username);

    /** Проверка наличия у пользователя прав администратора
     * <p> {@link ru.skypro.homework.dto.Role#ADMIN}<p/>
     */
    void checkAdmin(Authentication authentication);
}