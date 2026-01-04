package ru.skypro.homework.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.controller.AuthController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.support.AuthTestData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = AuthController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class

})
public class AuthControllerWebMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void login_ShouldReturnOk_WhenDataValid() throws Exception {
        Login login = AuthTestData.createFullLogin();
        when(authService.login(login.getUsername(), login.getPassword())).thenReturn(true);

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
                )
                .andExpect(status().isOk());
    }

    @Test
    void login_ShouldReturnOk_WhenDataInvalid() throws Exception {
        Login login = AuthTestData.createFullLogin();
        when(authService.login(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
                )
                .andExpect(status().isUnauthorized());
    }


    @Test
    void register_ShouldReturnCreated_WhenRegisterSuccessful() throws Exception {
        Register register = AuthTestData.createFullRegister();
        when(authService.register(any(Register.class))).thenReturn(true);
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void register_ShouldReturnBadRequest_WhenServiceDisagree() throws Exception {
        Register register = AuthTestData.createFullRegister();
        when(authService.register(any(Register.class))).thenReturn(false);
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
                )
                .andExpect(status().isBadRequest());
    }


}
