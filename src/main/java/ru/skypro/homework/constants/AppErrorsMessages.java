package ru.skypro.homework.constants;

/**
 * Централизованное хранилище строковых констант для сообщений об ошибках.
 *
 * <p>Класс используется для обеспечения единообразия ответов API, упрощения
 * локализации и поддержки чистоты кода. Все константы являются неизменяемыми.</p>
 */

public final class AppErrorsMessages {

    /**
     * Ошибки безопасности и доступа.
     */

    public static final String INVALID_CREDENTIALS = "Invalid login or password";
    public static final String ACCESS_DENIED = "Access Denied";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String ONLY_ADMIN_ACCESS = "Only Admin access";
    public static final String INVALID_PASSWORD = "Invalid password";
    public static final String AUTH_DATA_NOT_FOUND = "Auth data not found";


    /**
     * Ошибки поиска сущностей.
     */

    public static final String USER_NOT_FOUND = "User not found";
    public static final String AD_NOT_FOUND = "Ad not found";
    public static final String COMMENT_NOT_FOUND = "Comment not found";
    public static final String INVALID_RELATION = "Invalid relation ad->comment";


    /**
     * Ошибки файловой системы и загрузки контента.
     */

    public static final String FILE_STORAGE_ERROR = "File storage error";
    public static final String UNSUPPORTED_FILE_TYPE = "Unsupported file type";
    public static final String FILE_TOO_BIG = "File too big";
    public static final String FILE_NOT_FOUND = "File not found";


    private AppErrorsMessages() {
    }
}
