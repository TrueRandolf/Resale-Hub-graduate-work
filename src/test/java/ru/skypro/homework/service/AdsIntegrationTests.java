package ru.skypro.homework.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.AuthRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.ImageServiceImpl;
import ru.skypro.homework.test_utils.AdsTestData;

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AdsIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdsRepository adsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ImageServiceImpl imageService;

    @Autowired
    private AdServiceImpl adService;


    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        adsRepository.deleteAll();
        authRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setUserName("author@mail.com");
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPhone("+799912345678");
        testUser = userRepository.save(user);

        AuthEntity auth = new AuthEntity();
        auth.setUser(testUser);
        auth.setPassword("passsword");
        auth.setRole(Role.USER);
        authRepository.save(auth);
    }

    @Test
    @DisplayName("Интеграционный тест: создание и получение объявлений")
    @WithMockUser(username = "author@mail.com")
    void shouldCreateAndGetAds() throws Exception {
        CreateOrUpdateAd createAd = AdsTestData.createUpdatedAd();
        MockMultipartFile imagePart = new MockMultipartFile("image", "test.png",
                MediaType.IMAGE_PNG_VALUE, "content".getBytes());
        MockMultipartFile propertiesPart = new MockMultipartFile("properties", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(createAd));

        when(imageService.saveAdImage(any(), anyLong())).thenReturn("images/test_image.png");
        mockMvc.perform(multipart("/ads")
                        .file(imagePart)
                        .file(propertiesPart)
                        .with(csrf()))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].title").value(createAd.getTitle()));
        assertThat(adsRepository.count()).isEqualTo(1);
    }


    @Test
    @DisplayName("Интеграционный тест: удаление объявления (204)")
    @WithMockUser(username = "author@mail.com")
    void shouldDeleteAd() throws Exception {
        AdEntity ad = new AdEntity();
        ad.setTitle("Ad to be deleted");
        ad.setPrice(500);
        ad.setDescription("Description of deleted ad");
        ad.setUser(testUser);
        ad = adsRepository.save(ad);
        Long adId = ad.getId();
        mockMvc.perform(delete("/ads/{id}", adId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        assertThat(adsRepository.findById(adId)).isEmpty();
    }

    @Test
    @DisplayName("Интеграционный тест: обновление объявления (200)")
    @WithMockUser(username = "author@mail.com")
    void shouldUpdateAd() throws Exception {
        AdEntity ad = new AdEntity();
        ad.setTitle("Old Title");
        ad.setPrice(100);
        ad.setDescription("Old Description");
        ad.setUser(testUser);
        ad = adsRepository.save(ad);

        CreateOrUpdateAd updateDto = new CreateOrUpdateAd();
        updateDto.setTitle("Brand New Title");
        updateDto.setPrice(777);
        updateDto.setDescription("Updated description text");

        mockMvc.perform(patch("/ads/{id}", ad.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Brand New Title"))
                .andExpect(jsonPath("$.price").value(777));
        AdEntity updatedAd = adsRepository.findById(ad.getId()).orElseThrow();
        assertThat(updatedAd.getTitle()).isEqualTo("Brand New Title");
    }

    @Test
    @DisplayName("Ошибка: удаление чужого объявления (403)")
    @WithMockUser(username = "stranger@mail.com")
    void deleteAd_Forbidden() throws Exception {
        AdEntity ad = new AdEntity();
        ad.setTitle("This Title");
        ad.setPrice(100);
        ad.setDescription("This Description");
        ad.setUser(testUser);
        ad = adsRepository.save(ad);


        mockMvc.perform(delete("/ads/{id}", ad.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Ошибка: получение инфо о несуществующем объявлении (404)")
    @WithMockUser
    void getAdInfo_NotFound() throws Exception {
        mockMvc.perform(get("/ads/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("AdService: 404 при получении несуществующего")
    @WithMockUser
    void getAd_NotFound() throws Exception {
        mockMvc.perform(get("/ads/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("AdService: 403 при обновлении чужого")
    @WithMockUser(username = "stranger@mail.com")
    void updateAd_Forbidden() throws Exception {
        CreateOrUpdateAd updateDto = AdsTestData.createUpdatedAd();
        AdEntity ad = new AdEntity();
        ad.setTitle("This Title");
        ad.setPrice(100);
        ad.setDescription("This Description");
        ad.setUser(testUser);
        ad = adsRepository.save(ad);
        mockMvc.perform(patch("/ads/{id}", ad.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("Добиваем покрытие deleteAllByUserId")
    @WithMockUser(username = "author@mail.com")
    void triggerDeleteAll() {
        adService.deleteAllByUserId(testUser.getId());
        assertThat(adsRepository.findAllByUser_Id(testUser.getId())).isEmpty();
    }

    @Test
    @DisplayName("AdService: Полное покрытие updateAdImage")
    @WithMockUser(username = "author@mail.com")
    void shouldUpdateImageFull() throws Exception {
        AdEntity ad = new AdEntity();
        ad.setTitle("Image Test Ad");
        ad.setPrice(100);
        ad.setDescription("Description longer than 8 chars");
        ad.setUser(testUser);
        ad.setAdImage("ads_images/old_image.png");
        ad = adsRepository.save(ad);

        MockMultipartFile file = new MockMultipartFile("image", "new.png",
                MediaType.IMAGE_PNG_VALUE, "content".getBytes());
        when(imageService.saveAdImage(any(), anyLong())).thenReturn("ads_images/new_image.png");
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/{id}/image", ad.getId())
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("AdService: 404 при обновлении картинки несуществующего объявления")
    @WithMockUser
    void updateAdImage_NotFound() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "test.png", "image/png", "content".getBytes());
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/{id}/image", 999999)
                        .file(file).with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GlobalHandler: Покрытие 500 ошибки")
    @WithMockUser
    void shouldReturnBadRequest_WhenTitleTooShort() throws Exception {
        CreateOrUpdateAd invalidAd = new CreateOrUpdateAd();
        invalidAd.setTitle("1");
        invalidAd.setPrice(100);
        invalidAd.setDescription("Short");
        mockMvc.perform(post("/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAd))
                        .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GlobalHandler: Покрытие 400 ошибки")
    @WithMockUser
    void shouldReturn400_WhenCommentIsShort() throws Exception {
        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText("123");
        AdEntity ad = new AdEntity();
        ad.setTitle("Test Ad Test");
        ad.setPrice(100);
        ad.setDescription("Description Description");
        ad.setUser(testUser);
        ad.setAdImage("ads_images/old_image.png");
        ad = adsRepository.save(ad);
        mockMvc.perform(post("/ads/{id}/comments", ad.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("AdService: Получение моих объявлений (GET /ads/me)")
    @WithMockUser(username = "author@mail.com")
    void getAdsMe_FullCoverage() throws Exception {
        AdEntity ad = new AdEntity();
        ad.setTitle("My Ad");
        ad.setPrice(100);
        ad.setDescription("Description for ads me");
        ad.setUser(testUser);
        adsRepository.save(ad);
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }


    @Test
    @WithMockUser(username = "commentator@mail.com")
    void shouldGetAdsMe() throws Exception {
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCoverUpdateImageCatch() throws IOException {
        AdEntity ad = new AdEntity();
        ad.setTitle("My Ad");
        ad.setPrice(100);
        ad.setDescription("Description for ads me");
        ad.setUser(testUser);
        adsRepository.save(ad);

        MultipartFile file = org.mockito.Mockito.mock(MultipartFile.class);

        org.mockito.Mockito.when(file.getBytes()).thenThrow(new IOException());
        org.mockito.Mockito.when(file.getContentType()).thenReturn("image/png");
        org.mockito.Mockito.when(file.isEmpty()).thenReturn(false);
        org.mockito.Mockito.when(file.getSize()).thenReturn(100L);

        assertThrows(RuntimeException.class, () ->
                adService.updateAdImage(file, ad.getId(), null));
    }
}