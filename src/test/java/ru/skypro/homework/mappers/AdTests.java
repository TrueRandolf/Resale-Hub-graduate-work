package ru.skypro.homework.mappers;


import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.test_utils.TestData;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class AdTests {
    private final AdMapper adMapper = org.mapstruct.factory.Mappers.getMapper(AdMapper.class);

    @Test
    void shouldMapExtendedAdFromAdEntity() {
        UserEntity userEntity = TestData.createTestUserEntity();
        AdEntity adEntity = TestData.createTestAdEntity(userEntity);

        ExtendedAd extendedAd = adMapper.toExtendedAd(adEntity);

        assertThat(extendedAd).isNotNull();
        assertThat(extendedAd.getPk().longValue()).isEqualTo(adEntity.getId());
        assertThat(extendedAd.getAuthorFirstName()).isEqualTo(userEntity.getFirstName());
        assertThat(extendedAd.getAuthorLastName()).isEqualTo(userEntity.getLastName());
        assertThat(extendedAd.getDescription()).isEqualTo(adEntity.getDescription());
        assertThat(extendedAd.getEmail()).isEqualTo(userEntity.getUserName());
        assertThat(extendedAd.getImage()).isEqualTo(adEntity.getAdImage());
        assertThat(extendedAd.getPhone()).isEqualTo(userEntity.getPhone());
        assertThat(extendedAd.getPrice()).isEqualTo(adEntity.getPrice());
        assertThat(extendedAd.getTitle()).isEqualTo(adEntity.getTitle());
    }

    @Test
    void shouldMapAdsFromListAdEntities() {
        List<AdEntity> adEntityList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserEntity userEntity = TestData.createTestUserEntity();
            AdEntity adEntity = TestData.createTestAdEntity(userEntity);
            adEntityList.add(adEntity);
        }

        Ads ads = adMapper.toAds(adEntityList);

        assertThat(ads).isNotNull();
        assertThat(ads.getCount()).isEqualTo(adEntityList.size());
        assertThat(ads.getResults()).isNotEmpty();
    }

}