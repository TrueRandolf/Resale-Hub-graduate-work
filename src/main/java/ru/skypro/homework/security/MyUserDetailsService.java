package ru.skypro.homework.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.exceptions.UnauthorizedException;
import ru.skypro.homework.repository.AuthRepository;

/**
 * Сервис аутентификации пользователей.
 *
 * <p>Загружает данные из БД и проверяет статус профиля.
 * При отсутствии записи или наличии метки удаления выбрасывает {@link UnauthorizedException}.</p>
 */

@Slf4j
@AllArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final AuthRepository authRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UnauthorizedException {
        log.debug("Attempting to authenticate user: {}", username);

        AuthEntity authEntity = authRepository.findByUser_UserName(username)
                .orElseThrow(() -> {
                    log.warn("Authentication failed: User {} not found", username);
                    return new UnauthorizedException("Invalid login/password");
                });

        if (authEntity.getUser().getDeletedAt() != null) {
            log.warn("Authentication failed: Account {} is deleted", username);
            throw new UnauthorizedException("Invalid login/password");
        }

        return User.builder()
                .username(authEntity.getUser().getUserName())
                .password(authEntity.getPassword())
                .roles(authEntity.getRole().name())
                .build();
    }
}
