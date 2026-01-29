package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

/**
 * Сервис аутентификации и регистрации пользователей.
 *
 * <p>Обеспечивает проверку учетных данных при входе и
 * создание новых учетных записей в системе.</p>
 */

public interface AuthService {

    /**
     * Проверка учетных данных пользователя.
     *
     * @param userName логин пользователя.
     * @param password пароль пользователя.
     * @throws ru.skypro.homework.exceptions.UnauthorizedException если логин или пароль неверны.
     */
    void login(String userName, String password);

    /**
     * Регистрация нового пользователя в системе.
     * @param register объект с данными профиля и учетными данными.
     */
    void register(Register register);
}
