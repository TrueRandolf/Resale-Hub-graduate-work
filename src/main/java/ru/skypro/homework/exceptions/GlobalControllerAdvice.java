package ru.skypro.homework.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skypro.homework.dto.ErrorDto;


/**
 * Глобальный обработчик исключений для REST-контроллеров приложения.
 * Обеспечивает централизованную обработку исключений внутри приложения
 * и стандартизированные ответы на ошибки {@link ErrorDto} клиенту.
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {


    /**
     * Обрабатывает исключения {@link IllegalArgumentException}, возникающие при вводе некорректных данных.
     * Возвращает HTTP статус BAD_REQUEST (400).
     *
     * @param e Перехваченное исключение.
     * @return Ответ с текстом ошибки и кодом статуса 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorDto errorResponse = new ErrorDto(
                HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        log.error("BAD_REQUEST {}", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключения {@link NullPointerException}, возникающие при обращении к неинициализированным полям.
     * Возвращает HTTP статус INTERNAL_SERVER_ERROR (500).
     *
     * @param e Перехваченное исключение.
     * @return Ответ с текстом ошибки и кодом статуса 500.
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException(NullPointerException e) {
        ErrorDto errorResponse = new ErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(), "");
        log.error("INTERNAL_SERVER_ERROR {}", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Обрабатывает исключения {@link com.fasterxml.jackson.databind.exc.InvalidFormatException},
     * возникающие при попытке десериализации JSON, когда типы данных в JSON не соответствуют
     * ожидаемым типам в DTO.
     * Возвращает HTTP статус BAD_REQUEST (400).
     *
     * @param e Перехваченное исключение.
     * @return Ответ с описанием ошибки и статусом 400.
     */
    @ExceptionHandler(com.fasterxml.jackson.databind.exc.InvalidFormatException.class)
    public ResponseEntity<ErrorDto> handleInvalidFormatException(com.fasterxml.jackson.databind.exc.InvalidFormatException e) {
        ErrorDto errorResponse = new ErrorDto(
                HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    /**
     * Обрабатывает исключения {@link HttpMessageNotReadableException}, возникающие, когда
     * тело запроса не может быть прочитано или является синтаксически неверным JSON.
     * Возвращает HTTP статус BAD_REQUEST (400).
     *
     * @param e Перехваченное исключение.
     * @return Ответ с описанием ошибки и статусом 400.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorDto errorResponse = new ErrorDto(
                HttpStatus.BAD_REQUEST.toString(), "Bad JSON");
        log.error("BAD_REQUEST {}", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}


