package ru.skypro.homework.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.controller.AuthController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.test_utils.AuthTestsData;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles("test")
public class AuthControllerWebMVCTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Успешная авторизация (200)")
    void login_Success() throws Exception {
        Login login = new Login();
        login.setUsername(AuthTestsData.DEFAULT_USERNAME);
        login.setPassword(AuthTestsData.DEFAULT_PASSWORD);

        doNothing().when(authService).login(anyString(), anyString());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(authService).login(eq(login.getUsername()), eq(login.getPassword()));
    }

    @Test
    @DisplayName("Ошибка валидации логина: пароль слишком короткий (400)")
    void login_ValidationError() throws Exception {
        Login login = new Login();
        login.setUsername(AuthTestsData.DEFAULT_USERNAME);
        login.setPassword(AuthTestsData.INVALID_PASSWORD);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }


    @Test
    @DisplayName("Успешная регистрация (201)")
    void register_Success() throws Exception {
        Register register = AuthTestsData.createRegisterDto();

        doNothing().when(authService).register(any(Register.class));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Ошибка регистрации: неверный формат телефона (400)")
    void register_PhoneValidationError() throws Exception {
        Register register = AuthTestsData.createRegisterDto();
        register.setPhone(AuthTestsData.INVALID_PHONE);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

}

