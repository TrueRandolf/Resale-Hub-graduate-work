package ru.skypro.homework.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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
     * Обрабатывает все исключения-наследники от класса {@link AppException},
     * Возвращает HTTP статус и сообщение Message
     * @param e Перехваченное исключение.
     * @return Ответ с описанием ошибки и статусом ошибки.
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDto> handleAppException(AppException e) {
        log.error("AppException : {}-{}", e.getStatus(), e.getMessage());

        ErrorDto errorDto = new ErrorDto(e.getStatus().toString(), e.getMessage());

        return new ResponseEntity<>(errorDto, e.getStatus());
    }


    /**
     * Обрабатывает ошибки валидации некорректных данных (аннотации  @Size, @Min, @Max и т.д).
     * Возвращает HTTP статус BAD_REQUEST (400).
     *
     * @param e Перехваченное исключение.
     * @return Ответ с текстом ошибки и кодом статуса 400.
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException e) {
        log.error("Validation error occurred");
        ErrorDto errorDto = new ErrorDto(
                HttpStatus.BAD_REQUEST.toString(),
                "Ошибка валидации данных. Проверьте правильность заполнения полей."
        );
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает превышение допустимого размера загружаемого файла.
     * <p>Максимальный допустимый размер указыается в настройках приложения. </p>
     *  Возвращает HTTP статус BAD_REQUEST (400).
     *
     * @param e Перехваченное исключение.
     * @return Ответ с текстом ошибки и кодом статуса 400.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorDto> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.error("Payload too large : {}", e.getMessage());
        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
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
        log.error("JSON deserializable error : {}", e.getMessage());
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
        log.error("JSON read or syntax {}", e.getMessage());
        ErrorDto errorResponse = new ErrorDto(
                HttpStatus.BAD_REQUEST.toString(), "Bad JSON");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

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
     * Обрабатывает все неучтенные ранее исключения {@link Exception}.
     * Возвращает HTTP статус INTERNAL_SERVER_ERROR (500).
     *
     * @param e Перехваченное исключение.
     * @return Ответ с текстом ошибки и кодом статуса 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneralException(Exception e) {
        log.error("INTERNAL_SERVER_ERROR {}", e.getMessage());
        ErrorDto errorResponse = new ErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Server Error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}


