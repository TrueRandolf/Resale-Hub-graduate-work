package ru.skypro.homework.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.repository.AuthRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
        authRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setUserName("user@mail.com");
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPhone("+79991234567");
        user = userRepository.save(user);

        AuthEntity auth = new AuthEntity();
        auth.setUser(user);
        auth.setPassword(passwordEncoder.encode("password"));
        auth.setRole(Role.USER);
        authRepository.save(auth);
    }

    @Test
    @DisplayName("Интеграционный тест: жизненный цикл пользователя")
    @WithMockUser(username = "user@mail.com")
    void userLifecycle_Success() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"));
        UpdateUser update = new UpdateUser();
        update.setFirstName("NewName");
        update.setLastName("NewLast");
        update.setPhone("+79998887766");
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("NewName"));
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("password");
        newPassword.setNewPassword("new_secret");
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword))
                        .with(csrf()))
                .andExpect(status().isOk());
        AuthEntity updatedAuth = authRepository.findByUser_UserName("user@mail.com").orElseThrow();
        assertThat(passwordEncoder.matches("new_secret", updatedAuth.getPassword())).isTrue();
    }


    @Test
    @DisplayName("UserService: 401 при неверном текущем пароле")
    @WithMockUser(username = "user@mail.com")
    void updatePassword_WrongCurrent() throws Exception {
        NewPassword np = new NewPassword();
        np.setCurrentPassword("WRONG_PASS");
        np.setNewPassword("new_pass123");
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(np))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("UserService: 404 если юзер не найден при обновлении")
    @WithMockUser(username = "ghost@mail.com")
    void updateAuthUser_UserNotFound() throws Exception {
        UpdateUser update = new UpdateUser();
        update.setFirstName("Ghost");

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

}
