package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Стандартизированное тело ответа при возникновении ошибки в API.
 * <p> Формируется и возвращается
 * {@link ru.skypro.homework.exceptions.GlobalControllerAdvice} </p>
 */

@Getter
@AllArgsConstructor
@Builder
public class ErrorDto {
    private final String code;
    private final String message;

}
