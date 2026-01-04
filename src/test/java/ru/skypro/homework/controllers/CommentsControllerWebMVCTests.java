package ru.skypro.homework.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.controller.CommentsController;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.support.AdsTestData;
import ru.skypro.homework.support.CommentsTestData;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CommentsController.class)
public class CommentsControllerWebMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser
    void getComments_ShouldReturnComments() throws Exception {
        Comments comments = CommentsTestData.createFullComments();
        List<Comment> commentsList = comments.getResults();

        mockMvc.perform(get("/ads/{id}/comments", CommentsTestData.COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(comments.getCount()))
                .andExpect(jsonPath("$.results").value(objectMapper.convertValue(commentsList, List.class)));
    }


    @Test
    @WithMockUser
    void addComment_ShouldReturnOkComment() throws Exception {
        CreateOrUpdateComment updateComment = CommentsTestData.createFullUpdateComment();

        mockMvc.perform(post("/ads/{id}/comments", CommentsTestData.COMMENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateComment))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(CommentsTestData.COMMENT_UPDATE));
    }

    @Test
    @WithMockUser
    void addShortComment_ShouldReturnBadRequest() throws Exception {
        CreateOrUpdateComment updateComment = CommentsTestData.createFullUpdateCommentShort();

        mockMvc.perform(post("/ads/{id}/comments", CommentsTestData.COMMENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateComment))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void addLongComment_ShouldReturnBadRequest() throws Exception {
        CreateOrUpdateComment updateComment = CommentsTestData.createFullUpdateCommentLong();

        mockMvc.perform(post("/ads/{id}/comments", CommentsTestData.COMMENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateComment))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void deleteComment_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", AdsTestData.AD_ID, CommentsTestData.COMMENT_ID)
                        .with(csrf())
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void updateComment_ShouldReturnOkComment() throws Exception {
        CreateOrUpdateComment updateComment = CommentsTestData.createFullUpdateComment();

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", AdsTestData.AD_ID, CommentsTestData.COMMENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateComment))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(CommentsTestData.COMMENT_UPDATE));
    }


    @Test
    @WithMockUser
    void updateShortComment_ShouldReturnBadRequest() throws Exception {
        CreateOrUpdateComment updateComment = CommentsTestData.createFullUpdateCommentShort();

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", AdsTestData.AD_ID, CommentsTestData.COMMENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateComment))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void updateLongComment_ShouldReturnBadRequest() throws Exception {
        CreateOrUpdateComment updateComment = CommentsTestData.createFullUpdateCommentLong();

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", AdsTestData.AD_ID, CommentsTestData.COMMENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateComment))
                )
                .andExpect(status().isBadRequest());
    }


}
