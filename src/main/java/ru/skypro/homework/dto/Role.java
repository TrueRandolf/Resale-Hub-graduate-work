package ru.skypro.homework.dto;

/**
 * Роли пользователей в системе.
 *
 * <p>Используются для разграничения прав доступа. Метод {@link #getRole()}
 * формирует строку с префиксом {@code ROLE_}, для корректной работы Spring Security.</p>
 */

public enum Role {
    USER, ADMIN;

    public String getRole() {
        return "ROLE_" + this.name();
    }
}
