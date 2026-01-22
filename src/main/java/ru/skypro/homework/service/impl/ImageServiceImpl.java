package ru.skypro.homework.service.impl;

import liquibase.pro.packaged.L;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.ErrorDto;
import ru.skypro.homework.service.ImageService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final Set<String> ALLOWED_IMAGETYPES = Set.of("jpg", "jpeg", "png", "webp");
    private final long MAX_SIZE = 10 * 1024 * 1024;

    @Value("${app.upload.main-dir}")
    private String mainDir;
    @Value("${app.upload.ads-dir}")
    private String adsDir;
    @Value("${app.upload.avatars-dir}")
    private String avatarsDir;

    private Path adsFilePath;
    private Path avatarsFilePath;


    @PostConstruct
    private void init() {
        adsFilePath = Path.of(mainDir, adsDir);
        avatarsFilePath = Path.of(mainDir, avatarsDir);
        try {
            log.info("created dir {}", Files.createDirectories(adsFilePath));
            log.info("created dir {}", Files.createDirectories(avatarsFilePath));
        } catch (IOException e) {
            log.error("Failed to create directory [{},{}] ", adsFilePath, avatarsFilePath, e);
            throw new UncheckedIOException("Failed to create directory", e);
        }
    }


    // обертки над
    public String saveAdImage(MultipartFile file, Long userId) {
        return saveImage(file, adsFilePath, adsDir, userId);
    }

    public String saveAvatarImage(MultipartFile file, Long userId) {
        return saveImage(file, avatarsFilePath, avatarsDir, userId);
    }
    //

    private String saveImage(MultipartFile file, Path targetDir, String subDir, Long userId) {
        String contentType = file.getContentType();
        if (file.isEmpty()) {
            log.error("Empty image try to load !");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect file");
        }

        if (file.getSize() > MAX_SIZE) {
            log.error("File too big!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File too big!");
        }

        if (contentType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown file type");
        }

        String extension = contentType.substring(contentType.lastIndexOf("/") + 1).toLowerCase();
        if (!ALLOWED_IMAGETYPES.contains(extension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file type");
        }

        String fileName = String.format("%s_%s.%s", userId, UUID.randomUUID(), extension);

        Path filePath = targetDir.resolve(fileName);

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("File save error {} ", filePath, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail save file");
        }
        log.info("Saved image path successfully: {}", fileName);
        return subDir + "/" + fileName;
    }

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
            log.error("Error file delete ! {}", filePath, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error disk file delete");
        }
    }

}
