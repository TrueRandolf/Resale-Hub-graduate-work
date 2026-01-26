package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.constants.AppErrorsMessages;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.exceptions.BadRequestException;
import ru.skypro.homework.exceptions.UnauthorizedException;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.repository.AuthRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Реализация сервиса аутентификации и регистрации.
 *
 * <p>Класс объединяет работу с профилями пользователей {@link UserEntity}
 * и данными авторизации {@link AuthEntity}. Использование {@link Transactional}
 * при регистрации гарантирует атомарность создания обеих сущностей.</p>
 */

@Slf4j
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     * <p>Сверка пароля происходит путем сравнения входящей строки с хэшем из БД
     * с помощью {@link PasswordEncoder}.</p>
     */
    @Override
    public void login(String userName, String password) {
        authRepository.findByUser_UserName(userName)
                .filter(a->encoder.matches(password, a.getPassword()))
                .orElseThrow(()->{
                    log.warn("Authentication failed for user: {}",userName);
                    return new UnauthorizedException(AppErrorsMessages.INVALID_CREDENTIALS);
                });

//                .map(a -> encoder.matches(password, a.getPassword()))
//                .orElse( ()-> { log.warn("Invalid password");
//                    throw new UnauthorizedException(AppErrorsMessages.INVALID_CREDENTIALS);
//                });

    }

    /**
     * {@inheritDoc}
     * <p>При регистрации выполняется шифрование пароля. Если роль не указана,
     * по умолчанию назначается {@link Role#USER}. Проверка уникальности логина
     * предотвращает создание дубликатов.</p>
     */
    @Transactional
    @Override
    public void register(Register register) {
        if (userRepository.existsByUserName(register.getUsername())) {
            log.warn("User already exists");
            throw new BadRequestException(AppErrorsMessages.USER_ALREADY_EXISTS);
        }
        UserEntity userEntity = userMapper.toUserEntity(register);

        UserEntity userSaved = userRepository.save(userEntity);
        AuthEntity authEntity = AuthEntity.builder()
                .user(userSaved)
                .password(encoder.encode(register.getPassword()))
                .role(register.getRole() == null ? Role.USER : register.getRole())
                .build();
        authRepository.save(authEntity);
        log.info("Successfully registered new user {} ", register.getUsername());
    }

}
