package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.constants.AppErrorsMessages;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.exceptions.NotFoundException;
import ru.skypro.homework.exceptions.UnauthorizedException;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.AuthRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.AccessServiceImpl;
import ru.skypro.homework.service.UserService;

/**
 * Реализация сервиса управления пользователями.
 *
 * <p>Обеспечивает редактирование профиля, смену пароля и многоуровневое удаление.
 * Включает интеграцию с сервисами безопасности и обработки изображений.</p>
 */

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    private final ImageServiceImpl imageService;
    private final AccessServiceImpl accessService;
    private final AdServiceImpl adService;
    private final AdsRepository adsRepository;

    /**
     * {@inheritDoc}
     * <p>Проверяет текущий пароль перед хэшированием и сохранением нового.</p>
     */
    @Override
    @Transactional
    public void updateUserPassword(NewPassword newPassword, Authentication authentication) {
        log.debug("invoked user service change password");

        accessService.checkAuth(authentication);
        String login = authentication.getName();

        AuthEntity authEntity = authRepository.findByUser_UserName(login)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.USER_NOT_FOUND));

        if (!encoder.matches(newPassword.getCurrentPassword(), authEntity.getPassword())) {
            throw new UnauthorizedException(AppErrorsMessages.INVALID_PASSWORD);
        }

        authEntity.setPassword(encoder.encode(newPassword.getNewPassword()));
        authRepository.save(authEntity);

    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public User getAuthUserInfo(Authentication authentication) {
        log.debug("invoked user service get info");

        String login = authentication.getName();
        log.info("user login: {}", login);

        accessService.checkAuth(authentication);

        AuthEntity authEntity = authRepository.findByUser_UserName(login)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.AUTH_DATA_NOT_FOUND));
        UserEntity userEntity = authEntity.getUser();

        return userMapper.toUserDto(userEntity, authEntity);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public UpdateUser updateAuthUser(UpdateUser updateUser, Authentication authentication) {
        log.debug("invoked user service update info");

        UserEntity userEntity = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.USER_NOT_FOUND));

        accessService.checkAuth(authentication);

        userMapper.updateUserEntity(updateUser, userEntity);
        userRepository.save(userEntity);

        return userMapper.toDtoUpdateUser(userEntity);
    }


    /**
     * {@inheritDoc}
     * <p>Выполняет замену аватара с последующим удалением старого файла с диска.</p>
     */
    @Override
    @Transactional
    public void updateAuthUserImage(MultipartFile file, Authentication authentication) {
        log.debug("invoked user service update image");

        UserEntity userEntity = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.USER_NOT_FOUND));

        accessService.checkAuth(authentication);

        String previousImage = userEntity.getUserImage();
        String newImage = imageService.saveAvatarImage(file, userEntity.getId());
        userEntity.setUserImage(newImage);
        userRepository.save(userEntity);
        if (!(previousImage == null || previousImage.isBlank())) {
            imageService.deleteImage(previousImage);
        }

    }
}
