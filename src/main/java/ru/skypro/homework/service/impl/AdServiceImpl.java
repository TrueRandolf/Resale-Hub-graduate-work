package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.support.AdsTestData;

import java.util.Objects;

@Slf4j
@Service
public class AdServiceImpl implements AdService {

    public Ads getAds() {
        log.info("invoked ad service getAllAds");
        return AdsTestData.createFullAds();
    }

    public Ad adSimpleAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image) {
        log.info("invoked ad service ad ad");
        if (createOrUpdateAd == null || image == null || Objects.requireNonNull(image.getOriginalFilename()).isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Ad ad = AdsTestData.createFullAd();
        ad.setPk(AdsTestData.ANOTHER_AD_ID);
        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setPrice(createOrUpdateAd.getPrice());
        //ad.setImage(image.getOriginalFilename());
        // где-то тут будет вызов метода из имагесервис

        return ad;

    }

    public ExtendedAd getAdInfo(Long id) {
        log.info("invoked ad service get ad info");
        return AdsTestData.createFullExtendedAd();
    }

    public boolean deleteSimpleAd(Long id) {
        log.info("invoked ad service delete ad");
        return true;
    }

    public Ad updateSingleAd(Long id, CreateOrUpdateAd ad) {
        log.info("invoked ad service update ad");
        return AdsTestData.createFullAd();
    }

    public Ads getAllAdsAuthUser() {
        log.info("invoked ad service getAllAds user");
        return AdsTestData.createFullAds();
    }

    public boolean updateAdImage(Long id) {
        log.info("invoked ad service update image");
        return true;
    }

}
