package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.constants.AppErrorsMessages;
import ru.skypro.homework.dto.metric.BusinessMetric;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.exceptions.ForbiddenException;
import ru.skypro.homework.exceptions.NotFoundException;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.AuthRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.AccessService;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.ManagementService;

import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ManagementServiceImpl implements ManagementService {

    private final AccessService accessService;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final AdsRepository adsRepository;
    private final AdService adService;
    private final ImageService imageService;


    /**
     * {@inheritDoc}
     * <p>Анонимизирует профиль, устанавливает {@code deletedAt} и инициирует
     * удаление контента через {@link AdServiceImpl}.</p>
     */
    @Override
    @Transactional(readOnly = true)
    public BusinessMetric getBusinessMetric(Authentication authentication) {
        log.info("invoked business-metric method");
        accessService.checkAdmin(authentication);
        log.info("Authenticated user {}", authentication.getName());

        return BusinessMetric.builder()
                .totalUsers(userRepository.count())
                .activeUsers(userRepository.countByDeletedAtIsNull())
                .deletedUsers(userRepository.countByDeletedAtIsNotNull())
                .build();
    }


    /**
     * {@inheritDoc}
     * <p>Анонимизирует профиль, устанавливает {@code deletedAt} и инициирует
     * удаление контента через {@link AdServiceImpl}.</p>
     */
    @Override
    @Transactional
    public void softDeleteUser(Long id, Authentication authentication) {
        UserEntity userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.USER_NOT_FOUND));

        accessService.checkAdmin(authentication);
        log.warn("Admin {} initiated soft-delete for user id {}", authentication.getName(), id);

        checkSelfDeletion(id, authentication.getName());

        adService.deleteAllByUserId(id);

        String newName = "id" + id + "@deleted";
        String avatarPath = userToDelete.getUserImage();
        userToDelete.setUserName(newName);
        userToDelete.setUserImage(null);
        userToDelete.setDeletedAt(LocalDateTime.now());

        authRepository.deleteById(id);

        try {
            imageService.deleteImage(avatarPath);
        } catch (UncheckedIOException e) {
            log.error("ERROR! Can't remove user avatar {}", avatarPath, e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>Выполняет полное удаление сущности и всех связанных медиафайлов с диска.</p>
     */
    @Override
    @Transactional
    public void hardDeleteUser(Long id, Authentication authentication) {
        accessService.checkAdmin(authentication);
        log.warn("Admin {} initiated hard-delete for user id {}", authentication.getName(), id);

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.USER_NOT_FOUND));

        checkSelfDeletion(id, authentication.getName());

        List<AdEntity> adEntityList = adsRepository.findAllByUser_Id(id);
        Set<String> imageToDelete = adEntityList.stream()
                .map(AdEntity::getAdImage)
                .filter(i -> i != null && !i.isBlank())
                .collect(Collectors.toSet());
        if (userEntity.getUserImage() != null && !userEntity.getUserImage().isBlank())
            imageToDelete.add(userEntity.getUserImage());
        userRepository.delete(userEntity);

        imageToDelete.forEach(path -> {
            try {
                imageService.deleteImage(path);
            } catch (UncheckedIOException e) {
                log.error("ERROR! Can't remove image file {}", path, e);
            }
        });
    }

    private void checkSelfDeletion(Long targetId, String currentUsername) {
        UserEntity currentUser = userRepository.findByUserName(currentUsername)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.USER_NOT_FOUND));
        if (currentUser.getId().equals(targetId)) {
            log.error("Admin {} tried to delete themselves!", currentUsername);
            throw new ForbiddenException(AppErrorsMessages.ACCESS_DENIED);
        }
    }


}
