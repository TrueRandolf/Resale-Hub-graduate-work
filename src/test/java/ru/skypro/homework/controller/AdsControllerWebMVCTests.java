package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.exceptions.ForbiddenException;
import ru.skypro.homework.exceptions.NotFoundException;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.test_utils.AdsTestData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdsController.class)
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
public class AdsControllerWebMVCTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdService adService;

    @Test
    @DisplayName("Успешное получение всех объявлений авторизованным пользователем")
    @WithMockUser
    void getAllAds_StatusOk() throws Exception {
        int count = 2;
        Ads mockAds = AdsTestData.createdAds(count);

        when(adService.getAds(any(Authentication.class))).thenReturn(mockAds);
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results[0].pk").value(AdsTestData.AD_ID_START))
                .andExpect(jsonPath("$.results[0].title").value(AdsTestData.DEFAULT_TITLE + "0"))
                .andExpect(jsonPath("$.results[1].price").value(AdsTestData.DEFAULT_PRICE + 1));
    }

    @Test
    @DisplayName("Отказ в доступе неавторизованному пользователю")
    void getAllAds_Unauthorized() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("Успешное добавление объявления (201)")
    @WithMockUser
    void addAd_Success() throws Exception {
        CreateOrUpdateAd properties = AdsTestData.createUpdatedAd();
        Ad mockAd = AdsTestData.createAdDto(1);

        MockMultipartFile imageFile = new MockMultipartFile("image", "test.png",
                MediaType.IMAGE_PNG_VALUE, "image content".getBytes());

        MockMultipartFile propertiesPart = new MockMultipartFile("properties", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(properties));

        when(adService.addSimpleAd(any(), any(), any(Authentication.class))).thenReturn(mockAd);
        mockMvc.perform(multipart("/ads")
                        .file(imageFile)
                        .file(propertiesPart)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").value(mockAd.getPk()))
                .andExpect(jsonPath("$.title").value(mockAd.getTitle()));
    }

    @Test
    @DisplayName("Ошибка валидации: слишком короткий заголовок (400)")
    @WithMockUser
    void addAd_ValidationError() throws Exception {
        CreateOrUpdateAd properties = AdsTestData.createUpdatedAd();
        properties.setTitle("---");

        MockMultipartFile propertiesPart = new MockMultipartFile("properties", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(properties));

        MockMultipartFile imageFile = new MockMultipartFile("image", "test.png",
                MediaType.IMAGE_PNG_VALUE, "image content".getBytes());

        mockMvc.perform(multipart("/ads")
                        .file(imageFile)
                        .file(propertiesPart)
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(adService);
    }

    @Test
    @DisplayName("Добавление объявления неавторизованным пользователем (401)")
    void addAd_Unauthorized() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("image", "test.png",
                MediaType.IMAGE_PNG_VALUE, "image content".getBytes());

        mockMvc.perform(multipart("/ads")
                        .file(imageFile)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("Успешное получение объявления по ID (200)")
    @WithMockUser
    void getAds_Success() throws Exception {
        ExtendedAd mockExtendedAd = AdsTestData.createExtendedAd(0);
        Long expectedId = AdsTestData.AD_ID_START.longValue();

        when(adService.getAdInfo(eq(expectedId), any(Authentication.class))).thenReturn(mockExtendedAd);

        mockMvc.perform(get("/ads/{id}", AdsTestData.AD_ID_START))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(AdsTestData.AD_ID_START))
                .andExpect(jsonPath("$.title").value(AdsTestData.DEFAULT_TITLE + "0"));

        verify(adService).getAdInfo(eq(expectedId), any(Authentication.class));
    }

    @Test
    @DisplayName("Объявление не найдено (404)")
    @WithMockUser
    void getAds_NotFound() throws Exception {
        int id = 999;
        when(adService.getAdInfo(eq(Long.valueOf(id)), any(Authentication.class)))
                .thenThrow(new NotFoundException("Not Found"));

        mockMvc.perform(get("/ads/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное удаление объявления (204)")
    @WithMockUser
    void removeAd_Success() throws Exception {
        Long expectedId = AdsTestData.AD_ID_START.longValue();

        doNothing().when(adService).deleteSimpleAd(eq(expectedId), any(Authentication.class));
        mockMvc.perform(delete("/ads/{id}", AdsTestData.AD_ID_START)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(adService).deleteSimpleAd(eq(expectedId), any(Authentication.class));
    }

    @Test
    @DisplayName("Ошибка доступа при удалении (403)")
    @WithMockUser
    void removeAd_Forbidden() throws Exception {
        int id = AdsTestData.AD_ID_START;

        doThrow(new ForbiddenException("Forbidden"))
                .when(adService).deleteSimpleAd(eq(Long.valueOf(id)), any(Authentication.class));
        mockMvc.perform(delete("/ads/{id}", id)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("Успешное обновление объявления (200)")
    @WithMockUser
    void updateAds_Success() throws Exception {
        int id = AdsTestData.AD_ID_START;
        CreateOrUpdateAd updateDto = AdsTestData.createUpdatedAd();
        Ad mockAd = AdsTestData.createAdDto(0);
        mockAd.setTitle(AdsTestData.UPDATED_TITLE);

        when(adService.updateSingleAd(eq(Long.valueOf(id)), any(), any()))
                .thenReturn(mockAd);
        mockMvc.perform(patch("/ads/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(id))
                .andExpect(jsonPath("$.title").value(AdsTestData.UPDATED_TITLE));
    }

    @Test
    @DisplayName("Ошибка доступа при обновлении (403)")
    @WithMockUser
    void updateAds_Forbidden() throws Exception {
        int id = AdsTestData.AD_ID_START;
        CreateOrUpdateAd updateDto = AdsTestData.createUpdatedAd();

        when(adService.updateSingleAd(eq(Long.valueOf(id)), any(), any()))
                .thenThrow(new ForbiddenException("Forbidden"));
        mockMvc.perform(patch("/ads/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("Получение объявлений текущего пользователя (200)")
    @WithMockUser
    void getAdsMe_Success() throws Exception {
        int count = 3;
        Ads mockAds = AdsTestData.createdAds(count);

        when(adService.getAllAdsAuthUser(any(Authentication.class))).thenReturn(mockAds);
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(count))
                .andExpect(jsonPath("$.results").isArray());

        verify(adService).getAllAdsAuthUser(any(Authentication.class));
    }


    @Test
    @DisplayName("Успешное обновление картинки (200)")
    @WithMockUser
    void updateImage_Success() throws Exception {
        int id = AdsTestData.AD_ID_START;

        byte[] mockImage = "new image content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "image", "test.png", MediaType.IMAGE_PNG_VALUE, "content".getBytes());

        when(adService.updateAdImage(any(), eq(Long.valueOf(id)), any()))
                .thenReturn(mockImage);
        mockMvc.perform(multipart("/ads/{id}/image", id)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(content().bytes(mockImage));

        verify(adService).updateAdImage(any(), eq(Long.valueOf(id)), any());
    }


}

