package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void deleteImage(String filePath);

    String saveAdImage(MultipartFile file, Long userId);

    String saveAvatarImage(MultipartFile file, Long userId);
}
