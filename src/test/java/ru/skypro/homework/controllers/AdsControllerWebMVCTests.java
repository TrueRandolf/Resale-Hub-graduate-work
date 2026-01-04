package ru.skypro.homework.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.controller.AdsController;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.support.AdsTestData;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdsController.class)
public class AdsControllerWebMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser
    void getAllAds_ShouldReturnOkAds() throws Exception {
        Ads ads = AdsTestData.createFullAds();
        List<Ad> adList = ads.getResults();
        mockMvc.perform(get("/ads")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(ads.getCount()))
                .andExpect(jsonPath("$.results").value(objectMapper.convertValue(adList, List.class)));
    }


    @Test
    @WithMockUser
    void addAd_ShouldReturnCreateAd() throws Exception {
        CreateOrUpdateAd properties = AdsTestData.createFullUpdateAd();
        String jsonProperties = objectMapper.writeValueAsString(properties);

        MockMultipartFile propertiesPart = new MockMultipartFile(
                "properties",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                jsonProperties.getBytes());

        MockMultipartFile image = new MockMultipartFile(
                "image",
                AdsTestData.IMAGE_TEST_PATH,
                MediaType.IMAGE_JPEG_VALUE,
                "data".getBytes()
        );

        mockMvc.perform(multipart("/ads")
                        .file(propertiesPart)
                        .file(image)
                        .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(AdsTestData.UPDATE_TITLE))
                .andExpect(jsonPath("$.price").value(AdsTestData.UPDATE_PRICE))
                .andExpect(jsonPath("$.image").value(AdsTestData.IMAGE_TEST_PATH));
    }

    @Test
    @WithMockUser
    void addAd_ShouldReturnBadRequestWithoutImage() throws Exception {
        CreateOrUpdateAd properties = AdsTestData.createFullUpdateAd();
        String jsonProperties = objectMapper.writeValueAsString(properties);

        MockMultipartFile propertiesPart = new MockMultipartFile(
                "properties",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                jsonProperties.getBytes());

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        mockMvc.perform(multipart("/ads")
                        .file(propertiesPart)
                        .file(image)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void addAd_ShouldReturnBadRequestWhenInvalidAd() throws Exception {
        CreateOrUpdateAd properties = AdsTestData.createEmptyUpdateAd();
        String jsonProperties = objectMapper.writeValueAsString(properties);

        MockMultipartFile propertiesPart = new MockMultipartFile(
                "properties",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                jsonProperties.getBytes());

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        mockMvc.perform(multipart("/ads")
                        .file(propertiesPart)
                        .file(image)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAd_ShouldReturnUnauthorized() throws Exception {
        CreateOrUpdateAd properties = AdsTestData.createFullUpdateAd();
        String jsonProperties = objectMapper.writeValueAsString(properties);

        MockMultipartFile propertiesPart = new MockMultipartFile(
                "properties",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                jsonProperties.getBytes());

        MockMultipartFile image = new MockMultipartFile(
                "image",
                AdsTestData.IMAGE_TEST_PATH,
                MediaType.IMAGE_JPEG_VALUE,
                "data".getBytes()
        );

        mockMvc.perform(multipart("/ads")
                        .file(propertiesPart)
                        .file(image)
                        .with(csrf())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getAds_ShouldReturnOkExtendedAd() throws Exception {
        ExtendedAd extendedAd = AdsTestData.createFullExtendedAd();
        mockMvc.perform(get("/ads/{id}", AdsTestData.AD_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(extendedAd.getPk()))
                .andExpect(jsonPath("$.authorFirstName").value(extendedAd.getAuthorFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(extendedAd.getAuthorLastName()))
                .andExpect(jsonPath("$.description").value(extendedAd.getDescription()))
                .andExpect(jsonPath("$.email").value(extendedAd.getEmail()))
                .andExpect(jsonPath("$.phone").value(extendedAd.getPhone()))
                .andExpect(jsonPath("$.title").value(extendedAd.getTitle()))
                .andExpect(jsonPath("$.price").value(extendedAd.getPrice()));
    }

    @Test
    @WithMockUser
    void removeAd_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/ads/{id}", AdsTestData.AD_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeAd_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/ads/{id}", AdsTestData.AD_ID)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    void updateAds_ShouldReturnOkAd() throws Exception {
        CreateOrUpdateAd updateAd = AdsTestData.createFullUpdateAd();
        mockMvc.perform(patch("/ads/{id}", AdsTestData.AD_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAd))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updateAd.getTitle()))
                .andExpect(jsonPath("$.price").value(updateAd.getPrice()));
    }

    @Test
    @WithMockUser
    void updateAds_ShouldReturnBadRequest() throws Exception {
        CreateOrUpdateAd updateAd = null;
        mockMvc.perform(patch("/ads/{id}", AdsTestData.AD_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAd))
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateAds_ShouldReturnUnauthorized() throws Exception {
        CreateOrUpdateAd updateAd = AdsTestData.createFullUpdateAd();
        mockMvc.perform(patch("/ads/{id}", AdsTestData.AD_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAd))
                )
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    void getAdsMe_ShouldReturnOkAds() throws Exception {
        Ads ads = AdsTestData.createFullAds();
        List<Ad> adList = ads.getResults();
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(ads.getCount()))
                .andExpect(jsonPath("$.results").value(objectMapper.convertValue(adList, List.class)));
    }


    @Test
    void getAdsMe_ShouldReturnUnauthorized() throws Exception {
        Ads ads = AdsTestData.createFullAds();
        List<Ad> adList = ads.getResults();
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void updateImage_ShouldReturnOkImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                AdsTestData.IMAGE_TEST_PATH,
                MediaType.IMAGE_JPEG_VALUE,
                "content".getBytes()
        );
        mockMvc.perform(multipart("/ads/{id}/image", AdsTestData.AD_ID)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }


    @Test
    @WithMockUser
    void updateImage_ShouldReturnBadRequest_WhenImageIsEmpty() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                AdsTestData.IMAGE_TEST_PATH,
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );
        mockMvc.perform(multipart("/ads/{id}/image", AdsTestData.AD_ID)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateImage_ShouldReturnUnauthorized() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                AdsTestData.IMAGE_TEST_PATH,
                MediaType.IMAGE_JPEG_VALUE,
                "content".getBytes()
        );
        mockMvc.perform(multipart("/ads/{id}/image", AdsTestData.AD_ID)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }


}
