package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Класс стандартизированного ответа ошибки в API.
 * Возвращает клиенту информацию об ошибке в стандартизированном виде.
 */
@Getter
@AllArgsConstructor
@Builder
public class ErrorDto {
    private final String code;
    private final String message;

}
