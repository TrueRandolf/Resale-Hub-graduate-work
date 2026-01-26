package ru.skypro.homework.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Исключение для индикации некорректных данных в запросе (HTTP 400).
 *
 * <p>Выбрасывается при нарушении бизнес-логики или получении невалидных параметров.</p>
 * <p>Обрабатывается в {@link GlobalControllerAdvice}.</p>
 */

public class BadRequestException extends AppException {
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
