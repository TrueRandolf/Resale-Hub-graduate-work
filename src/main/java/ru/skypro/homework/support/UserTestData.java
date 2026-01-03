package ru.skypro.homework.support;

import lombok.experimental.UtilityClass;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;

@UtilityClass
public class UserTestData {
    public static final int USER_ID = 10;
    public static final String USER_EMAIL = "Qwert@mail.ru";
    public static final String USER_FIRST_NAME = "John";
    public static final String USER_LAST_NAME = "Doe";
    public static final String USER_PHONE = "+7 912 366 00 00";
    public static final String USER_ROLE = Role.USER.name();
    public static final String USER_IMAGE = "/image/avatar.jpg";

    public static final String USER_NEW_FIRST_NAME = "Mary";
    public static final String USER_NEW_LAST_NAME = "Sue";
    public static final String USER_NEW_PHONE = "+7 912 366 11 11";

    public User createEmtyUser() {
        return new User();
    }

    public User createFullUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setFirstName(USER_FIRST_NAME);
        user.setLastName(USER_LAST_NAME);
        user.setPhone(USER_PHONE);
        user.setRole(USER_ROLE);
        user.setImage(USER_IMAGE    );
        return user;
    }

    public UpdateUser createEmptyUpdateUser(){
        return new UpdateUser();
    }

    public UpdateUser createFullUpdateUser(){
        UpdateUser updateUser = new UpdateUser();
        updateUser.setPhone(USER_NEW_PHONE);
        updateUser.setFirstName(USER_NEW_FIRST_NAME);
        updateUser.setLastName(USER_NEW_LAST_NAME);
        return updateUser;
    }



}
