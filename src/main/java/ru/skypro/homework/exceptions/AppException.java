package ru.skypro.homework.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Базовое исключение для бизнес-логики приложения.
 *
 * <p>Позволяет связывать конкретные ошибки выполнения с соответствующими
 * HTTP-статусами, которые будут возвращены клиенту через {@link GlobalControllerAdvice}.</p>
 */

public abstract class AppException extends RuntimeException {
    private final HttpStatus httpStatus;

    protected AppException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
