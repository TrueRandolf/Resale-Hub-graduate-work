package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;

/**
 * Сервис для управления объявлениями.
 *
 * <p>Реализует бизнес-логику создания, изменения и удаления объявлений,
 * а также работу с их медиафайлами. Все операции требуют проверки
 * полномочий пользователя.</p>
 */

public interface AdService {

    /** Получение списка всех объявлений в системе. */
    Ads getAds(Authentication authentication);

    /**
     * Создание нового объявления.
     * @param ad данные (заголовок, цена, описание).
     * @param image файл изображения.
     * @param authentication данные пользователя из контекста безопасности.
     */
    Ad addSimpleAd(CreateOrUpdateAd ad, MultipartFile image, Authentication authentication);

    /** Получение полной информации об объявлении по его ID
     * @param id идентификатор объявления.
     * @param authentication данные пользователя из контекста безопасности.
     */
    ExtendedAd getAdInfo(Long id, Authentication authentication);

    /** Удаление объявления из системы.
     * @param id идентификатор объявления.
     * @param authentication данные пользователя из контекста безопасности.
     */
    void deleteSimpleAd(Long id, Authentication authentication);

    /**
     * Обновление текстовых данных объявления.
     * @param id идентификатор объявления.
     * @param ad обновляемые данные (заголовок, цена, описание).
     * @param authentication данные пользователя из контекста безопасности.
     */
    Ad updateSingleAd(Long id, CreateOrUpdateAd ad, Authentication authentication);

    /** Получение списка всех объявлений текущего пользователя */
    Ads getAllAdsAuthUser(Authentication authentication);

    /** Обновление основного изображения объявления.
     * @return бинарные данные обновленного изображения.
     */
    byte[] updateAdImage(MultipartFile file, Long id, Authentication authentication);

    /**
     * Служебный метод удаления всех объявлений пользователя.
     * @param userId идентификатор пользователя.
     */
    void deleteAllByUserId(Long userId);
}