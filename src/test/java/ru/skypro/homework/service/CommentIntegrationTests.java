package ru.skypro.homework.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.CommentEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CommentIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity author;
    private AdEntity testAd;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        adsRepository.deleteAll();
        userRepository.deleteAll();

        author = new UserEntity();
        author.setUserName("commentator@mail.com");
        author.setFirstName("Ivan");
        author.setLastName("Ivan");
        author.setPhone("+799912345678");
        userRepository.save(author);

        testAd = new AdEntity();
        testAd.setTitle("Ad for comments");
        testAd.setDescription("Description for ad for comments");
        testAd.setPrice(100);
        testAd.setUser(author);
        adsRepository.save(testAd);
    }

    @Test
    @DisplayName("Интеграционный тест: добавить, получить и удалить комментарий")
    @WithMockUser(username = "commentator@mail.com")
    void commentLifecycle_Success() throws Exception {
        CreateOrUpdateComment createDto = new CreateOrUpdateComment();
        createDto.setText("Very long and interesting comment");

        String response = mockMvc.perform(post("/ads/{id}/comments", testAd.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(createDto.getText()))
                .andReturn().getResponse().getContentAsString();

        Comment responseDto = objectMapper.readValue(response, Comment.class);
        Integer commentId = responseDto.getPk();

        mockMvc.perform(get("/ads/{id}/comments", testAd.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].pk").value(commentId));

        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", testAd.getId(), commentId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertThat(commentRepository.existsById(commentId.longValue())).isFalse();
    }


    @Test
    @DisplayName("Ошибка доступа: чужак не может удалить чужой коммент (403)")
    @WithMockUser(username = "stranger@mail.com")
    void deleteComment_Forbidden() throws Exception {
        CommentEntity comment = new CommentEntity();
        comment.setText("Author's comment");
        comment.setUser(author);
        comment.setAd(testAd);
        comment.setCreatedAt(170000000000L);
        comment = commentRepository.save(comment);

        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", testAd.getId(), comment.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("CommentService: 404 при неверной связи Ad-Comment")
    @WithMockUser(username = "commentator@mail.com")
    void deleteComment_InvalidRelation() throws Exception {
        AdEntity otherAd = new AdEntity();
        otherAd.setTitle("Other Ad");
        otherAd.setDescription("Other description");
        otherAd.setUser(author);
        otherAd = adsRepository.save(otherAd);

        CommentEntity comment = new CommentEntity();
        comment.setText("Author's comment");
        comment.setUser(author);
        comment.setAd(testAd);
        comment.setCreatedAt(170000000000L);
        comment = commentRepository.save(comment);

        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", otherAd.getId(), comment.getId())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("CommentService: 404 при удалении несуществующего комментария")
    @WithMockUser
    void deleteComment_NotFound() throws Exception {
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", testAd.getId(), 999999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GlobalHandler: Ошибка валидации текста (400)")
    @WithMockUser
    void shouldReturn400_WhenCommentTextIsShort() throws Exception {
        CreateOrUpdateComment invalidComment = new CreateOrUpdateComment();
        invalidComment.setText("123");

        mockMvc.perform(post("/ads/{id}/comments", testAd.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidComment))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("CommentService: Успешное обновление комментария (200)")
    @WithMockUser(username = "commentator@mail.com")
    void shouldUpdateComment() throws Exception {
        CommentEntity comment = new CommentEntity();
        comment.setText("Old Comment Text");
        comment.setUser(author);
        comment.setAd(testAd);
        comment.setCreatedAt(System.currentTimeMillis());
        comment = commentRepository.save(comment);

        CreateOrUpdateComment updateDto = new CreateOrUpdateComment();
        updateDto.setText("New Updated Comment Text");

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", testAd.getId(), comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("New Updated Comment Text"));

        CommentEntity updated = commentRepository.findById(comment.getId()).orElseThrow();
        assertThat(updated.getText()).isEqualTo("New Updated Comment Text");
    }

}

