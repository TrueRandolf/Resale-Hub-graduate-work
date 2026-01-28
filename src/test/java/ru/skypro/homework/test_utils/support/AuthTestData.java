package ru.skypro.homework.test_utils.support;

import lombok.experimental.UtilityClass;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;

@UtilityClass
public class AuthTestData {

    public static final String USER_NAME = "DukeN@yandex.ru";
    public static final String USER_PASSWORD = "dncornholio";

    public static final String USER_FIRST_NAME = "Duke";
    public static final String USER_LAST_NAME = "Nukem";
    public static final String USER_PHONE = "+79123669999";
    public static final Role USER_ROLE = Role.USER;


    public Login createEmptyLogin(){
        return new Login();
    }

    public Login createFullLogin(){
        Login login = new Login();
        login.setUsername(USER_NAME);
        login.setPassword(USER_PASSWORD);
        return login;
    }

    public Register createEmtyRegister(){
        return new Register();
    }

    public Register createFullRegister(){
        Register register = new Register();
        register.setUsername(USER_NAME);
        register.setPassword(USER_PASSWORD);
        register.setFirstName(USER_FIRST_NAME);
        register.setLastName(USER_LAST_NAME);
        register.setPhone(USER_PHONE);
        register.setRole(USER_ROLE);
        return register;
    }

}
