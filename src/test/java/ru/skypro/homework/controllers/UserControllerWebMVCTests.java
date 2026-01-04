package ru.skypro.homework.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.controller.UsersController;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.support.UserTestData;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
public class UserControllerWebMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void setPassword_ShouldReturnOk() throws Exception {
        mockMvc.perform(post("/users/set_password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserTestData.CreateValidPassword()))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void setShortPassword_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/set_password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserTestData.CreateTooShortPassword()))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void setLongPassword_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/set_password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserTestData.CreateTooLongPassword()))
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    void getUser_ShouldReturnUser() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(UserTestData.USER_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(UserTestData.USER_LAST_NAME))
                .andExpect(jsonPath("$.phone").value(UserTestData.USER_PHONE));
    }


    @Test
    @WithMockUser
    void updateUser_ShouldReturnUpdateUserData() throws Exception {
        UpdateUser updateUser = UserTestData.createFullUpdateUser();

        mockMvc.perform(patch("/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(UserTestData.USER_NEW_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(UserTestData.USER_NEW_LAST_NAME))
                .andExpect(jsonPath("$.phone").value(UserTestData.USER_NEW_PHONE));
    }

    @Test
    @WithMockUser
    void updateUser_ShouldReturnBadRequest_WhenBodyISEmpty() throws Exception {
        mockMvc.perform(patch("/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    void updateUserImage_ShouldReturnOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "content".getBytes()
        );

        mockMvc.perform(multipart("/users/me/image")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf())
                )
                .andExpect(status().isOk());

    }

}
