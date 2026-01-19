package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;

public interface AdService {
    Ads getAds();

    Ad adSimpleAd(CreateOrUpdateAd ad, MultipartFile image);

    ExtendedAd getAdInfo(Long id);

    boolean deleteSimpleAd(Long id);

    Ad updateSingleAd(Long id, CreateOrUpdateAd ad);

    Ads getAllAdsAuthUser();

    boolean updateAdImage(Long id);
}
