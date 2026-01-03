package ru.skypro.homework.support;

import lombok.experimental.UtilityClass;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class AdsTestData {
    public static final int AUTHOR_ID = 10;
    public static final String IMAGE_PATH = "/image/ad.jpg";
    public static final int AD_ID = 100;
    public static final int AD_PRICE = 1500;
    public static final String AD_TITLE = "Ad title";

    public static final String UPDATE_TITLE = "Update title";
    public static final int UPDATE_PRICE = 2500;
    public static final String UPDATE_DESCRIPTION = "Update description";

    public static final String AD_DESCRIPTION = "Ad description";


    public Ad createEmptyAd() {
        return new Ad();
    }

    public Ad createFullAd() {
        Ad ad = new Ad();
        ad.setAuthor(AUTHOR_ID);
        ad.setImage(IMAGE_PATH);
        ad.setPk(AD_ID);
        ad.setPrice(AD_PRICE);
        ad.setTitle(AD_TITLE);
        return ad;
    }

    public Ads createEmptyAds() {
        return new Ads();
    }

    public Ads createFullAds() {
        Ads ads = new Ads();
        List<Ad> adList = new ArrayList<>();
        adList.add(createFullAd());
        ads.setResults(adList);
        ads.setCount(adList.size());
        return ads;
    }

    public CreateOrUpdateAd createEmptyUpdateAd() {
        return new CreateOrUpdateAd();
    }

    public CreateOrUpdateAd createFullUpdateAd() {
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setTitle(UPDATE_TITLE);
        createOrUpdateAd.setPrice(UPDATE_PRICE);
        createOrUpdateAd.setDescription(UPDATE_DESCRIPTION);
        return createOrUpdateAd;
    }

    public ExtendedAd createEmptyExtendedAd() {
        return new ExtendedAd();
    }

    public ExtendedAd createFullExtendedAd() {
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(AD_ID);
        extendedAd.setAuthorFirstName(UserTestData.USER_FIRST_NAME);
        extendedAd.setAuthorLastName(UserTestData.USER_LAST_NAME);
        extendedAd.setDescription(AD_DESCRIPTION);
        extendedAd.setEmail(UserTestData.USER_EMAIL);
        extendedAd.setImage(IMAGE_PATH);
        extendedAd.setPhone(UserTestData.USER_PHONE);
        extendedAd.setPrice(AD_PRICE);
        extendedAd.setTitle(AD_TITLE);
        return extendedAd;
    }

}
