package ru.skypro.homework.mappers;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.test_utils.TestData;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AdMapperImpl.class)
@ActiveProfiles("test")
public class AdTests {

    @Autowired
    private AdMapper adMapper;

    @Value("${app.images.base-url}")
    private String testBaseUrl;

    @Test
    @DisplayName("Маппинг в расширенное DTO (ExtendedAd)")
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
        assertThat(extendedAd.getImage()).isEqualTo(testBaseUrl + adEntity.getAdImage());
        assertThat(extendedAd.getPhone()).isEqualTo(userEntity.getPhone());
        assertThat(extendedAd.getPrice()).isEqualTo(adEntity.getPrice());
        assertThat(extendedAd.getTitle()).isEqualTo(adEntity.getTitle());
    }

    @Test
    @DisplayName("Маппинг в список DTO (Ads)")
    void shouldMapAdsFromListAdEntities() {
        List<AdEntity> adEntityList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserEntity userEntity = TestData.createTestUserEntity();
            AdEntity adEntity = TestData.createTestAdEntity(userEntity);
            adEntityList.add(adEntity);
        }

        Ads ads = adMapper.toAds(adEntityList);
        System.out.println("ads = " + ads);

        assertThat(ads).isNotNull();
        assertThat(ads.getCount()).isEqualTo(adEntityList.size());
        assertThat(ads.getResults()).isNotEmpty();
    }


    @Test
    @DisplayName("Маппинг в краткое DTO (Ad)")
    void shouldMapAdDtoFromAdEntity() {
        UserEntity userEntity = TestData.createTestUserEntity();
        AdEntity adEntity = TestData.createTestAdEntity(userEntity);

        Ad adDto = adMapper.toAdDto(adEntity);

        assertThat(adDto).isNotNull();
        assertThat(adDto.getPk()).isEqualTo(adEntity.getId().intValue());
        assertThat(adDto.getAuthor()).isEqualTo(userEntity.getId().intValue());
        assertThat(adDto.getImage()).isEqualTo(testBaseUrl + adEntity.getAdImage());
        assertThat(adDto.getPrice()).isEqualTo(adEntity.getPrice());
        assertThat(adDto.getTitle()).isEqualTo(adEntity.getTitle());
    }

    @Test
    @DisplayName("Обновление существующей сущности из DTO (updateAdEntity)")
    void shouldUpdateAdEntityFromCreateOrUpdateAd() {

        AdEntity adEntity = new AdEntity();

        CreateOrUpdateAd updateDto = new CreateOrUpdateAd();
        updateDto.setTitle("New Title");
        updateDto.setPrice(500);
        updateDto.setDescription("New Description");

        adMapper.updateAdEntity(updateDto, adEntity);

        assertThat(adEntity.getTitle()).isEqualTo("New Title");
        assertThat(adEntity.getPrice()).isEqualTo(500);
        assertThat(adEntity.getDescription()).isEqualTo("New Description");
    }



}
