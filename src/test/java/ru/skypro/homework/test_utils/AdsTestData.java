package ru.skypro.homework.test_utils;

import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AdsTestData {

    public static final Integer DEFAULT_PRICE = 1000;
    public static final String DEFAULT_TITLE = "TEST AD ";
    public static final int DEFAULT_AUTHOR = 1;
    public static final String DEFAULT_IMAGE = "resale_image/ad_image/ad";
    public static final String DEFAULT_EXTENSION = ".png";
    public static final Integer AD_ID_START = 100;

    public static final String UPDATED_TITLE = "UPDATED TEST AD";
    public static final String UPDATED_DESCRIPTION = "UPDATED DESCRIPTION TEST AD";
    public static final Integer UPDATED_PRICE = 2000;

    public static String AUTHOR_FIRST_NAME = "AUTHOR FIRST NAME";
    public static String AUTHOR_LAST_NAME = "AUTHOR LAST NAME";
    public static final String DEFAULT_DESCRIPTION = "DEFAULT DESCRIPTION TEST AD";

    public static final String DEFAULT_LOGIN = "DEFAULT @LOGIN";
    public static final String DEFAULT_PHONE = "+799912345678";

    public static Ad createAdDto(int id) {
        Ad ad = new Ad();
        ad.setPk(AD_ID_START + id);
        ad.setAuthor(DEFAULT_AUTHOR);
        ad.setPrice(DEFAULT_PRICE + id);
        ad.setTitle(DEFAULT_TITLE + id);
        ad.setImage(DEFAULT_IMAGE + id + DEFAULT_EXTENSION);
        return ad;
    }

    public static Ads createdAds(int count) {
        List<Ad> list = IntStream.range(0, count)
                .mapToObj(AdsTestData::createAdDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setResults(list);
        ads.setCount(list.size());
        return ads;
    }

    public static CreateOrUpdateAd createUpdatedAd() {
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setDescription(UPDATED_DESCRIPTION);
        createOrUpdateAd.setTitle(UPDATED_TITLE);
        createOrUpdateAd.setPrice(UPDATED_PRICE);
        return createOrUpdateAd;
    }

    public static ExtendedAd createExtendedAd(int id) {
        ExtendedAd extendedAd = new ExtendedAd();

        extendedAd.setPk(AD_ID_START + id);
        extendedAd.setAuthorFirstName(AUTHOR_FIRST_NAME);
        extendedAd.setAuthorLastName(AUTHOR_LAST_NAME);
        extendedAd.setDescription(DEFAULT_DESCRIPTION);
        extendedAd.setEmail(DEFAULT_LOGIN);
        extendedAd.setImage(DEFAULT_IMAGE + id + DEFAULT_EXTENSION);
        extendedAd.setPhone(DEFAULT_PHONE);
        extendedAd.setPrice(DEFAULT_PRICE + id);
        extendedAd.setTitle(DEFAULT_TITLE + id);
        return extendedAd;

    }


}


