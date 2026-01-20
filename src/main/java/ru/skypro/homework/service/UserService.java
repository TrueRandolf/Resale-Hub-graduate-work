package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;

public interface UserService {
    void updateUserPassword(NewPassword newPassword, Authentication authentication);

    User getAuthUserInfo(Authentication authentication);

    UpdateUser updateAuthUser(UpdateUser updateUser, Authentication authentication);

    boolean updateAuthUserImage(String filepath,Authentication authentication);
}
