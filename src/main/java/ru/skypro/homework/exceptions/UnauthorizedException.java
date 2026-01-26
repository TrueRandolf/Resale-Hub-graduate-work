package ru.skypro.homework.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Исключение для индикации отсутствия аутентификации (HTTP 401).
 *
 * <p>Выбрасывается при попытке доступа неаутентифицированным пользователем к защищенному ресурсу. </p>
 * <p>Обрабатывается в {@link GlobalControllerAdvice}.</p>
 */

public class UnauthorizedException extends AppException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
