package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.support.UserTestData;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
//    boolean updateUserPassword(NewPassword newPassword);
//    User getAuthUserInfo();
//    User updateAuthUser(UpdateUser updateUser);
//    boolean updateAuthUserImage(String filepath


    public boolean updateUserPassword(NewPassword newPassword) {
        log.info("invoked user service userpassword");
        return true;
    }

    public User getAuthUserInfo() {
        log.info("invoked user service getinfo");
        return UserTestData.createFullUser();
    }


    public UpdateUser updateAuthUser(UpdateUser updateUser) {
        log.info("invoked user service update");
        if (updateUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        UpdateUser user = UserTestData.createEmptyUpdateUser();
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());

        return user;
    }

    public boolean updateAuthUserImage(String filepath) {
        log.info("invoked user service update image");
        return true;
    }


}
