package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Сервис для работы с изображениями.
 *
 * <p>Обеспечивает сохранение файлов на диск и удаление файлов с диска.
 * Используется для управления аватарами пользователей и фотографиями объявлений.</p>
 */

public interface ImageService {

    /**
     * Удаление файла изображения с диска.
     * @param filePath путь к файлу
     */
    void deleteImage(String filePath);

    /**
     * Сохранение изображения объявления.
     * @param file загружаемый файл
     * @param userId идентификатор автора для формирования пути
     * @return путь к сохраненному файлу
     */
    String saveAdImage(MultipartFile file, Long userId);

    /**
     * Сохранение аватара пользователя.
     * @param file загружаемый файл
     * @param userId идентификатор пользователя для формирования имени файла
     * @return путь к сохраненному файлу
     */
    String saveAvatarImage(MultipartFile file, Long userId);
}
