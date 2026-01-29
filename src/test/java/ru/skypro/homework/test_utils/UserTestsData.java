package ru.skypro.homework.test_utils;

import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;

public class UserTestsData {

    public static final Integer USER_ID = 1;
    public static final String EMAIL = "user@mail.com";
    public static final String DEFAULT_FIRST_NAME = "Ivan";
    public static final String DEFAULT_LAST_NAME = "Ivanov";
    public static final String DEFAULT_PHONE = "+79991234567";
    public static final String IMAGE_PATH = "resale_images/avatar_images/avatar.png";

    public static final String UPDATED_FIRST_NAME = "Agdam";
    public static final String UPDATED_LAST_NAME = "Agdamov";
    public static final String UPDATED_PHONE = "+79997654321";


    public static User createUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(EMAIL);
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setPhone(DEFAULT_PHONE);
        user.setRole(Role.USER.toString());
        user.setImage(IMAGE_PATH);
        return user;
    }

    public static UpdateUser createUpdateUser() {
        UpdateUser update = new UpdateUser();
        update.setFirstName(UPDATED_FIRST_NAME);
        update.setLastName(UPDATED_LAST_NAME);
        update.setPhone(UPDATED_PHONE);
        return update;
    }
}
