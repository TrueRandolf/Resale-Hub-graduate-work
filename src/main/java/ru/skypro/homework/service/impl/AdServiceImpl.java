package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.constants.AppErrorsMessages;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.exceptions.NotFoundException;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.AccessService;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса управления объявлениями.
 *
 * <p>Обеспечивает интеграцию между репозиторием объявлений, хранилищем файлов
 * и сервисом контроля доступа. Все методы защищены проверкой полномочий.</p>
 */

@AllArgsConstructor
@Slf4j
@Service
public class AdServiceImpl implements AdService {

    private final UserRepository userRepository;
    private final AdsRepository adsRepository;
    private final AdMapper mapper;
    private final AccessService accessService;
    private final ImageService imageService;

    /**{@inheritDoc}*/
    @Override
    @Transactional(readOnly = true)
    public Ads getAds(Authentication authentication) {
        log.debug("invoked ad service getAllAds");
        accessService.checkAuth(authentication);
        return mapper.toAds(adsRepository.findAll());
    }

    /**
     * {@inheritDoc}
     * <p>Выполняет атомарную операцию: сохранение файла на диск и запись пути в БД.</p>
     */
    @Override
    @Transactional
    public Ad addSimpleAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, Authentication authentication) {
        log.debug("invoked ad service add ad");

        accessService.checkAuth(authentication);

        UserEntity userEntity = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.USER_NOT_FOUND));

        AdEntity adEntity = mapper.toEntity(createOrUpdateAd);
        adEntity.setUser(userEntity);

        String imagePath = imageService.saveAdImage(image, userEntity.getId());
        adEntity.setAdImage(imagePath);
        adsRepository.save(adEntity);
        log.info("Ad saved. Image part: {}", imagePath);
        return mapper.toAdDto(adEntity);

    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getAdInfo(Long id, Authentication authentication) {
        log.debug("invoked ad service get ad info");

        accessService.checkAuth(authentication);

        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.AD_NOT_FOUND));
        return mapper.toExtendedAd(adEntity);
    }

    /**
     * {@inheritDoc}
     * <p>Перед удалением проверяет права доступа (автор или админ).
     * После удаления записи из БД удаляет связанный файл с диска.</p>
     */
    @Override
    @Transactional
    public void deleteSimpleAd(Long id, Authentication authentication) {
        log.debug("invoked ad service delete ad");

        accessService.checkAuth(authentication);
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.AD_NOT_FOUND));
        accessService.checkEdit(authentication, adEntity.getUser().getUserName());

        String filePath = adEntity.getAdImage();
        adsRepository.deleteById(id);
        if (filePath != null) imageService.deleteImage(filePath);

    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public Ad updateSingleAd(Long id, CreateOrUpdateAd ad, Authentication authentication) {
        log.debug("invoked ad service update ad");

        accessService.checkAuth(authentication);

        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.AD_NOT_FOUND));

        accessService.checkEdit(authentication, adEntity.getUser().getUserName());

        mapper.updateAdEntity(ad, adEntity);
        adsRepository.save(adEntity);
        return mapper.toAdDto(adEntity);

    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Ads getAllAdsAuthUser(Authentication authentication) {
        log.debug("invoked ad service getAllAds user");
        accessService.checkAuth(authentication);
        String login = authentication.getName();
        return mapper.toAds(adsRepository.findByUser_UserNameAndUserDeletedAtIsNull(login));
    }

    /**
     * {@inheritDoc}
     * <p>Обновляет путь к изображению в БД и удаляет старый файл с диска
     * только после успешного сохранения нового.</p>
     */
    @Override
    @Transactional
    public byte[] updateAdImage(MultipartFile file, Long id, Authentication authentication) {
        log.debug("invoked ad service update image");

        accessService.checkAuth(authentication);

        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.AD_NOT_FOUND));

        accessService.checkEdit(authentication, adEntity.getUser().getUserName());

        Long userId = adEntity.getUser().getId();

        String previousImage = adEntity.getAdImage();
        String newImage = imageService.saveAdImage(file, userId);
        adEntity.setAdImage(newImage);
        adsRepository.save(adEntity);
        if (!(previousImage == null || previousImage.isBlank())) {
            imageService.deleteImage(previousImage);
        }
        try {

            return file.getBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(AppErrorsMessages.FILE_STORAGE_ERROR, e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>Служба массовой очистки.
     *  <ul>
     *  <li>Собирает список всех путей к файлам контента пользователя.</li>
     *  <li>Очищает БД от объявлений.</li>
     *  <li>Удаляет файлы контента физически.</li>
     *  </ul>
     *  </p>
     * Используется в методе мягкого удаления пользователя
     * {@link ru.skypro.homework.service.ManagementService#softDeleteUser(Long, Authentication)}
     */
    @Override
    @Transactional
    public void deleteAllByUserId(Long userId) {
        log.warn("invoked service delete ads");
        List<AdEntity> adEntityList = adsRepository.findAllByUser_Id(userId);
        Set<String> imageToDelete = adEntityList.stream()
                .map(AdEntity::getAdImage)
                .filter(i -> i != null && !i.isBlank())
                .collect(Collectors.toSet());
        adsRepository.deleteByUser_Id(userId);

        imageToDelete.forEach(path -> {
            try {
                imageService.deleteImage(path);
            } catch (UncheckedIOException e) {
                log.error("ERROR! Can't remove image file {}", path, e);
            }
        });

    }
}