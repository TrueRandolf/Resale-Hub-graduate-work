package ru.skypro.homework.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Исключение для индикации отстутвия ресурса (HTTP 404).
 *
 * <p>Выбрасывается, если запрашиваемый объектне найден в БД.</p>
 * <p>Обрабатывается в {@link GlobalControllerAdvice}.</p>
 */


public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
