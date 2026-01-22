package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.repository.AuthRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    @Transactional
    public void updateUserPassword(NewPassword newPassword, Authentication authentication) {
        log.info("invoked user service user password");

        System.out.println("newPassword.getCurrentPassword() = " + newPassword.getCurrentPassword());
        System.out.println("newPassword.getNewPassword() = " + newPassword.getNewPassword());

        String login = authentication.getName();

        AuthEntity authEntity = authRepository.findByUser_UserName(login)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        if (!encoder.matches(newPassword.getCurrentPassword(), authEntity.getPassword())) {
            throw new IllegalArgumentException("wrong password");
        }

        authEntity.setPassword(encoder.encode(newPassword.getNewPassword()));
        authRepository.save(authEntity);

    }

    @Transactional(readOnly = true)
    public User getAuthUserInfo(Authentication authentication) {
        log.info("invoked user service get info");

        String login = authentication.getName();
        log.info("user login: {}", login);
        AuthEntity authEntity = authRepository.findByUser_UserName(login)
                .orElseThrow(() -> new UsernameNotFoundException("auth not found"));
        UserEntity userEntity = authEntity.getUser();

        return userMapper.toUserDto(userEntity, authEntity);
    }


    @Transactional
    public UpdateUser updateAuthUser(UpdateUser updateUser, Authentication authentication) {
        log.info("invoked user service update");

        UserEntity userEntity = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        userMapper.updateUserEntity(updateUser, userEntity);
        userRepository.save(userEntity);

        return userMapper.toDtoUpdateUser(userEntity);
    }


    public boolean updateAuthUserImage(String filepath, Authentication authentication) {
        log.info("invoked user service update image");
        return true;
    }


}
