package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.exceptions.ForbiddenException;
import ru.skypro.homework.exceptions.NotFoundException;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.test_utils.AdsTestData;
import ru.skypro.homework.test_utils.CommentsTestsData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CommentsController.class)
@ActiveProfiles("test")
public class CommentsControllerWebMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;


    @Test
    @DisplayName("Успешное получение комментариев (200)")
    @WithMockUser
    void getComments_Success() throws Exception {

        Comments mockComments = CommentsTestsData.createCommentsDto(2);

        Long adId = AdsTestData.AD_ID_START.longValue();

        when(commentService.getAllCommentsAd(eq(adId), any(Authentication.class)))
                .thenReturn(mockComments);
        mockMvc.perform(get("/ads/{id}/comments", AdsTestData.AD_ID_START))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results[0].pk").value(CommentsTestsData.START_ID));
    }

    @Test
    @DisplayName("Комментарии не найдены — объявление отсутствует (404)")
    @WithMockUser
    void getComments_NotFound() throws Exception {
        Long adId = 999L;

        when(commentService.getAllCommentsAd(eq(adId), any(Authentication.class)))
                .thenThrow(new NotFoundException("Ad not found"));
        mockMvc.perform(get("/ads/{id}/comments", 999))
                .andExpect(status().isNotFound());
    }


    /**********************************************************/

    @Test
    @DisplayName("Успешное добавление комментария (200)")
    @WithMockUser
    void addComment_Success() throws Exception {
        CreateOrUpdateComment requestDto = CommentsTestsData.createUpdateCommentDto();
        Comment mockResponse = CommentsTestsData.createCommentDto(0);

        when(commentService.addCommentToAd(eq(AdsTestData.AD_ID_START.longValue()), any(), any()))
                .thenReturn(mockResponse);
        mockMvc.perform(post("/ads/{id}/comments", AdsTestData.AD_ID_START)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(CommentsTestsData.START_ID))
                .andExpect(jsonPath("$.text").value(CommentsTestsData.DEFAULT_TEXT));
    }

    @Test
    @DisplayName("Ошибка: текст комментария слишком короткий (400)")
    @WithMockUser
    void addComment_ValidationError() throws Exception {
        CreateOrUpdateComment shortComment = new CreateOrUpdateComment();
        shortComment.setText("short");

        mockMvc.perform(post("/ads/{id}/comments", AdsTestData.AD_ID_START)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shortComment))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(commentService);
    }

    @Test
    @DisplayName("Добавление комментария к несуществующему объявлению (404)")
    @WithMockUser
    void addComment_AdNotFound() throws Exception {
        CreateOrUpdateComment requestDto = CommentsTestsData.createUpdateCommentDto();

        when(commentService.addCommentToAd(eq(999L), any(), any()))
                .thenThrow(new NotFoundException("Ad not found"));

        mockMvc.perform(post("/ads/{id}/comments", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Успешное удаление комментария (204)")
    @WithMockUser
    void deleteComment_Success() throws Exception {

        Long adId = AdsTestData.AD_ID_START.longValue();
        Long commentId = CommentsTestsData.START_ID.longValue();

        doNothing().when(commentService).deleteComment(eq(adId), eq(commentId), any(Authentication.class));

        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}",
                        AdsTestData.AD_ID_START, CommentsTestsData.START_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(commentService).deleteComment(eq(adId), eq(commentId), any(Authentication.class));
    }

    @Test
    @DisplayName("Ошибка доступа при удалении комментария (403)")
    @WithMockUser
    void deleteComment_Forbidden() throws Exception {
        doThrow(new ForbiddenException("Forbidden"))
                .when(commentService).deleteComment(anyLong(), anyLong(), any());

        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1, 1)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Комментарий или объявление не найдены (404)")
    @WithMockUser
    void deleteComment_NotFound() throws Exception {
        doThrow(new NotFoundException("Not Found"))
                .when(commentService).deleteComment(anyLong(), anyLong(), any());

        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 999, 999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Успешное обновление комментария (200)")
    @WithMockUser
    void updateComment_Success() throws Exception {
        CreateOrUpdateComment updateDto = CommentsTestsData.createUpdateCommentDto();
        Comment mockResponse = CommentsTestsData.createCommentDto(0);
        mockResponse.setText(CommentsTestsData.UPDATED_TEXT);

        when(commentService.updateComment(
                eq(AdsTestData.AD_ID_START.longValue()),
                eq(CommentsTestsData.START_ID.longValue()),
                any(), any()))
                .thenReturn(mockResponse);

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}",
                        AdsTestData.AD_ID_START, CommentsTestsData.START_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(CommentsTestsData.START_ID))
                .andExpect(jsonPath("$.text").value(CommentsTestsData.UPDATED_TEXT));
    }

    @Test
    @DisplayName("Ошибка доступа при обновлении (403)")
    @WithMockUser
    void updateComment_Forbidden() throws Exception {
        CreateOrUpdateComment updateDto = CommentsTestsData.createUpdateCommentDto();

        when(commentService.updateComment(anyLong(), anyLong(), any(), any()))
                .thenThrow(new ForbiddenException("Forbidden"));

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

}