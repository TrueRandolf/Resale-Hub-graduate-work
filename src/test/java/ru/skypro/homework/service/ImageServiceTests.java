package ru.skypro.homework.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.exceptions.BadRequestException;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ImageServiceImpl.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ImageServiceTests {

    @Autowired
    private ImageServiceImpl imageService;
    @Value("${app.upload.ads-dir}")
    private String adsDir;

    @Value("${app.upload.avatars-dir}")
    private String avatarsDir;

    @Value("${app.upload.main-dir}")
    private String mainDir;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Успешное сохранение и удаление изображения")
    void shouldSaveAndDeleteImage() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "image", "avatar.png", "image/png", "test-content".getBytes());

        String resultPath = imageService.saveAdImage(file, 1L);
        assertThat(resultPath).startsWith(adsDir + "/");
        Path physicalPath = Path.of(mainDir).resolve(resultPath);
        assertThat(Files.exists(physicalPath)).isTrue();
        imageService.deleteImage(resultPath);
        assertThat(Files.exists(physicalPath)).isFalse();
    }

    @Test
    @DisplayName("Ошибка: пустой файл (400)")
    void shouldThrowBadRequestWhenFileIsEmpty() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "image", "empty.png", "image/png", new byte[0]);

        assertThrows(BadRequestException.class, () -> imageService.saveAdImage(emptyFile, 1L));
    }

    @Test
    @DisplayName("Ошибка: недопустимый тип файла (400)")
    void shouldThrowBadRequestWhenTypeNotAllowed() {
        MockMultipartFile txtFile = new MockMultipartFile(
                "text", "test.txt", "text/plain", "content".getBytes());
        assertThrows(BadRequestException.class, () -> imageService.saveAdImage(txtFile, 1L));
    }

}