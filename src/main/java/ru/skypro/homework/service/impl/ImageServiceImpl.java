package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.constants.AppErrorsMessages;
import ru.skypro.homework.exceptions.BadRequestException;
import ru.skypro.homework.service.ImageService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

/**
 * Реализация сервиса управления файловой системой.
 *
 * <p>Отвечает за физическое хранение изображений на диске.
 * Включает проверку MIME-типов, ограничение размера файлов и
 * автоматическое создание структуры директорий при запуске.</p>
 */

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${app.upload.max-size}")
    private long maxSize;
    @Value("${app.upload.allowed-types}")
    private Set<String> allowedImagesTypes;

    @Value("${app.upload.main-dir}")
    private String mainDir;
    @Value("${app.upload.ads-dir}")
    private String adsDir;
    @Value("${app.upload.avatars-dir}")
    private String avatarsDir;

    private Path adsFilePath;
    private Path avatarsFilePath;

    /**
     * Инициализация хранилища.
     * <p>Создает необходимые папки для объявлений и аватаров, если они отсутствуют.
     * Использует базовый путь из конфигурации {@code app.upload.main-dir}.</p>
     */
    @PostConstruct
    private void init() {
        adsFilePath = Path.of(mainDir, adsDir);
        avatarsFilePath = Path.of(mainDir, avatarsDir);
        try {
            log.info("created dir {}", Files.createDirectories(adsFilePath));
            log.info("created dir {}", Files.createDirectories(avatarsFilePath));
        } catch (IOException e) {
            log.error("Failed to create directory [{},{}]", adsFilePath, avatarsFilePath, e);
            throw new UncheckedIOException(AppErrorsMessages.FILE_STORAGE_ERROR, e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>Генерирует уникальное имя файла изображения с использованием {@link UUID}
     * для предотвращения коллизий. </p>
     */
    @Override
    public String saveAdImage(MultipartFile file, Long userId) {
        return saveImage(file, adsFilePath, adsDir, userId);
    }


    /**
     * {@inheritDoc}
     * <p>Генерирует уникальное имя файла аватара пользователя с использованием {@link UUID}
     * для предотвращения коллизий. </p>
     */
    @Override
    public String saveAvatarImage(MultipartFile file, Long userId) {
        return saveImage(file, avatarsFilePath, avatarsDir, userId);
    }

    /**
     * Внутренний метод для сохранения файлов.
     * <p>Проверяет размер, MIME-тип и выполняет копирование потока байтов на диск.</p>
     */
    private String saveImage(MultipartFile file, Path targetDir, String subDir, Long userId) {
        String contentType = file.getContentType();
        if (file.isEmpty()) {
            log.error("Empty image try to load !");
            throw new BadRequestException(AppErrorsMessages.UNSUPPORTED_FILE_TYPE);
        }

        if (file.getSize() > maxSize) {
            log.error("File too big!");
            throw new BadRequestException(AppErrorsMessages.FILE_TOO_BIG);
        }

        if (contentType == null) {
            log.error("Unknown file type");
            throw new BadRequestException(AppErrorsMessages.UNSUPPORTED_FILE_TYPE);
        }

        String extension = contentType.substring(contentType.lastIndexOf("/") + 1).toLowerCase();
        if (!allowedImagesTypes.contains(extension)) {
            log.error("Unsupported file type");
            throw new BadRequestException(AppErrorsMessages.UNSUPPORTED_FILE_TYPE);
        }

        String fileName = String.format("%s_%s.%s", userId, UUID.randomUUID(), extension);

        Path filePath = targetDir.resolve(fileName);

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("File save error {}", filePath, e);
            throw new UncheckedIOException(AppErrorsMessages.FILE_STORAGE_ERROR, e);
        }
        log.info("Save image path successfully: {}", filePath);
        return subDir + "/" + fileName;
    }

    /**
     * {@inheritDoc}
     * <p>Выполняет физическое удаление файла с диска.
     * Если файл отсутствует, операция завершается без исключения с логированием предупреждения.</p>
     */
    @Override
    public void deleteImage(String filePath) {
        log.info("Delete image by path: {}", filePath);
        if (filePath == null || filePath.isEmpty()) {
            return;
        }
        try {
            Path path = Path.of(mainDir).resolve(filePath);
            if (Files.deleteIfExists(path)) {
                log.info("File successfully deleted {}", path);
            } else {
                log.warn("Filepath not found! {}", path);
            }
        } catch (IOException e) {
            log.error("Error file delete! {}", filePath, e);
            throw new UncheckedIOException(AppErrorsMessages.FILE_NOT_FOUND, e);
        }
    }

}
