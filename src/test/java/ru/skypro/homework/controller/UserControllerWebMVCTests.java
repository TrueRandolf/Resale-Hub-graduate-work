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
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;
import ru.skypro.homework.exceptions.ForbiddenException;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.test_utils.UserTestsData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
public class UserControllerWebMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;


    @Test
    @DisplayName("Успешное получение профиля (200)")
    @WithMockUser(username = UserTestsData.EMAIL)
    void getUser_Success() throws Exception {

        User mockUser = UserTestsData.createUser();

        when(userService.getAuthUserInfo(any(Authentication.class))).thenReturn(mockUser);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(UserTestsData.USER_ID))
                .andExpect(jsonPath("$.email").value(UserTestsData.EMAIL))
                .andExpect(jsonPath("$.firstName").value(UserTestsData.DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.image").value(UserTestsData.IMAGE_PATH));

        verify(userService).getAuthUserInfo(any(Authentication.class));
    }

    @Test
    @DisplayName("Отказ в доступе анониму (401)")
    void getUser_Unauthorized() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("Успешная смена пароля (200)")
    @WithMockUser
    void setPassword_Success() throws Exception {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("oldPassword123");
        newPassword.setNewPassword("newPassword123");

        doNothing().when(userService).updateUserPassword(any(NewPassword.class), any(Authentication.class));

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(userService).updateUserPassword(any(NewPassword.class), any(Authentication.class));
    }

    @Test
    @DisplayName("Ошибка валидации: слишком короткий пароль (400)")
    @WithMockUser
    void setPassword_ValidationError() throws Exception {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("short");
        newPassword.setNewPassword("short");

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Ошибка смены пароля (403)")
    @WithMockUser
    void setPassword_Forbidden() throws Exception {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("wrongPassword");
        newPassword.setNewPassword("newPassword123");

        doThrow(new ForbiddenException("Forbidden"))
                .when(userService).updateUserPassword(any(), any());

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }




    @Test
    @DisplayName("Успешное обновление профиля (200)")
    @WithMockUser
    void updateUser_Success() throws Exception {

        UpdateUser updateDto = UserTestsData.createUpdateUser();

        when(userService.updateAuthUser(any(UpdateUser.class), any(Authentication.class)))
                .thenReturn(updateDto);

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(UserTestsData.UPDATED_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(UserTestsData.UPDATED_LAST_NAME))
                .andExpect(jsonPath("$.phone").value(UserTestsData.UPDATED_PHONE));

        verify(userService).updateAuthUser(any(UpdateUser.class), any(Authentication.class));
    }

    @Test
    @DisplayName("Ошибка валидации: имя слишком короткое (400)")
    @WithMockUser
    void updateUser_ValidationError() throws Exception {
        UpdateUser invalidUpdate = UserTestsData.createUpdateUser();
        invalidUpdate.setFirstName("Jo");

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }


    @Test
    @DisplayName("Успешное обновление аватара (200)")
    @WithMockUser
    void updateUserImage_Success() throws Exception {

        MockMultipartFile image = new MockMultipartFile(
                "image", "avatar.png", MediaType.IMAGE_PNG_VALUE, "test image content".getBytes()
        );

        doNothing().when(userService).updateAuthUserImage(any(), any());

        mockMvc.perform(multipart("/users/me/image")
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(userService).updateAuthUserImage(any(), any());
    }

}
