package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;

public interface AdService {
    Ads getAds(Authentication authentication);

    Ad addSimpleAd(CreateOrUpdateAd ad, MultipartFile image, Authentication authentication);

    ExtendedAd getAdInfo(Long id, Authentication authentication);

    void deleteSimpleAd(Long id,Authentication authentication);

    Ad updateSingleAd(Long id, CreateOrUpdateAd ad, Authentication authentication);

    Ads getAllAdsAuthUser(Authentication authentication);

    byte[] updateAdImage(MultipartFile file, Long id, Authentication authentication);

    void deleteAllByUserId(Long userId);
}
