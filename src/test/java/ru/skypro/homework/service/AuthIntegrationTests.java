package ru.skypro.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.exceptions.BadRequestException;
import ru.skypro.homework.exceptions.UnauthorizedException;
import ru.skypro.homework.repository.AuthRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthIntegrationTests {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    @DisplayName("Успешная регистрация и логин")
    void registerAndLogin_Success() {
        Register register = new Register();
        register.setUsername("newuser@mail.com");
        register.setPassword("password123");
        register.setFirstName("Ivan");
        register.setFirstName("Ivanov");
        register.setPhone("+79991234567");
        register.setRole(Role.USER);

        authService.register(register);

        assertThat(userRepository.existsByUserName("newuser@mail.com")).isTrue();
        assertThat(authRepository.findByUser_UserName("newuser@mail.com")).isPresent();
        assertDoesNotThrow(() -> authService.login("newuser@mail.com", "password123"));
    }

    @Test
    @DisplayName("Ошибка регистрации: пользователь уже существует (400)")
    void register_ThrowsBadRequest_WhenUserExists() {

        UserEntity user = new UserEntity();
        user.setUserName("exists@mail.com");
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPhone("+79991112233");
        userRepository.save(user);

        Register register = new Register();
        register.setUsername("exists@mail.com");
        register.setPassword("password123");
        register.setFirstName("Ivan");
        register.setFirstName("Ivanov");
        register.setPhone("+79991234567");
        register.setRole(Role.USER);

        assertThrows(BadRequestException.class, () -> authService.register(register));
    }

    @Test
    @DisplayName("Ошибка логина: неверный пароль (401)")
    void login_ThrowsUnauthorized_WhenPasswordWrong() {
        Register register = new Register();
        register.setUsername("test@mail.com");
        register.setPassword("secret");
        register.setFirstName("Ivan");
        register.setFirstName("Ivanov");
        register.setPhone("+79991234567");
        register.setRole(Role.USER);

        authService.register(register);
        assertThrows(UnauthorizedException.class, () -> authService.login("test@mail.com", "wrong_pass"));
    }
}
