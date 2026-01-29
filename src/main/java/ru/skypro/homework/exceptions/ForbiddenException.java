package ru.skypro.homework.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Исключение для индикации отсутствия прав доступа (HTTP 403).
 *
 * <p>Выбрасывается при попытке выполнения аутентифицированным пользователем действия,
 * на которое у него нет прав (редактирование чужого контента и т.п.)</p>
 * <p>Обрабатывается в {@link GlobalControllerAdvice}.</p>
 */

public class ForbiddenException extends AppException {
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
