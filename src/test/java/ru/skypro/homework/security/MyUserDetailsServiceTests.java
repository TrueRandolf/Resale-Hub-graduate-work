package ru.skypro.homework.security;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.exceptions.UnauthorizedException;
import ru.skypro.homework.repository.AuthRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTests {

    @Mock
    private AuthRepository authRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Test
    @DisplayName("Успешная загрузка UserDetails")
    void loadUser_Success() {
        UserEntity user = new UserEntity();
        user.setUserName("test@mail.com");

        AuthEntity auth = new AuthEntity();
        auth.setUser(user);
        auth.setPassword("hash");
        auth.setRole(Role.USER);

        when(authRepository.findByUser_UserName("test@mail.com")).thenReturn(Optional.of(auth));

        UserDetails result = myUserDetailsService.loadUserByUsername("test@mail.com");

        assertThat(result.getUsername()).isEqualTo("test@mail.com");
        assertThat(result.getPassword()).isEqualTo("hash");
    }



    @Test
    @DisplayName("Ошибка: пользователь не найден")
    void loadUser_NotFound() {
        when(authRepository.findByUser_UserName("none")).thenReturn(Optional.empty());
        assertThrows(UnauthorizedException.class, () -> myUserDetailsService.loadUserByUsername("none"));
    }

    @Test
    @DisplayName("Ошибка: аккаунт помечен как удаленный")
    void loadUser_Deleted() {
        UserEntity user = new UserEntity();
        user.setDeletedAt(LocalDateTime.now()); // Метка удаления
        AuthEntity auth = new AuthEntity();
        auth.setUser(user);

        when(authRepository.findByUser_UserName("deleted")).thenReturn(Optional.of(auth));
        assertThrows(UnauthorizedException.class, () -> myUserDetailsService.loadUserByUsername("deleted"));
    }

}
