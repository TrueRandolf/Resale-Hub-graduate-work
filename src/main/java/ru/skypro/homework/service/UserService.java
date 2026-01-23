package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;

public interface UserService {
    void updateUserPassword(NewPassword newPassword, Authentication authentication);

    User getAuthUserInfo(Authentication authentication);

    UpdateUser updateAuthUser(UpdateUser updateUser, Authentication authentication);

    void updateAuthUserImage(MultipartFile file, Authentication authentication);

    void softDeleteUser(Long id, Authentication authentication);
}
