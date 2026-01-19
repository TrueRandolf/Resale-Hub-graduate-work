package ru.skypro.homework.service;

import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;

public interface UserService {
    boolean updateUserPassword(NewPassword newPassword);

    User getAuthUserInfo();

    UpdateUser updateAuthUser(UpdateUser updateUser);

    boolean updateAuthUserImage(String filepath);
}
