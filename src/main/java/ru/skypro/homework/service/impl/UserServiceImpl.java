package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;
import ru.skypro.homework.entities.AdEntity;
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

import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Transactional
    public void updateUserPassword(NewPassword newPassword, Authentication authentication) {
        log.info("invoked user service change password");

        accessService.checkAuth(authentication);
        String login = authentication.getName();

        AuthEntity authEntity = authRepository.findByUser_UserName(login)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!encoder.matches(newPassword.getCurrentPassword(), authEntity.getPassword())) {
            throw new UnauthorizedException("Wrong password");
        }

        authEntity.setPassword(encoder.encode(newPassword.getNewPassword()));
        authRepository.save(authEntity);

    }

    @Transactional(readOnly = true)
    public User getAuthUserInfo(Authentication authentication) {
        log.info("invoked user service get info");

        String login = authentication.getName();
        log.info("user login: {}", login);

        accessService.checkAuth(authentication);

        AuthEntity authEntity = authRepository.findByUser_UserName(login)
                .orElseThrow(() -> new NotFoundException("Auth data not found"));
        UserEntity userEntity = authEntity.getUser();

        return userMapper.toUserDto(userEntity, authEntity);
    }


    @Transactional
    public UpdateUser updateAuthUser(UpdateUser updateUser, Authentication authentication) {
        log.info("invoked user service update info");

        UserEntity userEntity = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new NotFoundException("user not found"));

        accessService.checkAuth(authentication);

        userMapper.updateUserEntity(updateUser, userEntity);
        userRepository.save(userEntity);

        return userMapper.toDtoUpdateUser(userEntity);
    }


    @Transactional
    public void updateAuthUserImage(MultipartFile file, Authentication authentication) {
        log.info("invoked user service update image");

        UserEntity userEntity = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        accessService.checkAuth(authentication);

        String previousImage = userEntity.getUserImage();
        String newImage = imageService.saveAvatarImage(file, userEntity.getId());
        userEntity.setUserImage(newImage);
        userRepository.save(userEntity);
        if (!(previousImage == null || previousImage.isBlank())) {
            imageService.deleteImage(previousImage);
        }

    }


    // MAINTENANCE SECTION
    // BEWARE THE JUBBERWOCK, MY SON !!!
    // delete user

    @Transactional
    public void softDeleteUser(Long id, Authentication authentication) {
        log.info("invoked soft-delete user by id {} !", id);
        UserEntity userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        accessService.checkEdit(authentication, userToDelete.getUserName());

        //POINT OF NO RETURN !!!
        adService.deleteAllByUserId(id);

        String newName = id + "@deleted";
        String avatarPath = userToDelete.getUserImage();
        userToDelete.setUserName(newName);
        userToDelete.setUserImage(null);
        userToDelete.setDeletedAt(LocalDateTime.now());

        authRepository.deleteById(id);

        try {
            imageService.deleteImage(avatarPath);
        } catch (UncheckedIOException e) {
            log.error("ERROR! Can't remove user avatar {}", avatarPath, e);
        }
    }

    @Transactional
    public void hardDeleteUser(Long id, Authentication authentication) {
        log.warn("invoked hard-delete user {} !", id);
        accessService.checkAdmin(authentication);

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<AdEntity> adEntityList = adsRepository.findAllByUser_Id(id);
        Set<String> imageToDelete = adEntityList.stream()
                .map(AdEntity::getAdImage)
                .filter(i -> i != null && !i.isBlank())
                .collect(Collectors.toSet());
        if (userEntity.getUserImage() != null && !userEntity.getUserImage().isBlank())
            imageToDelete.add(userEntity.getUserImage());
        //POINT OF NO RETURN !!!
        userRepository.delete(userEntity);

        imageToDelete.forEach(path -> {
            try {
                imageService.deleteImage(path);
            } catch (UncheckedIOException e) {
                log.error("ERROR! Can't remove image file {}", path, e);
            }
        });
    }

}
