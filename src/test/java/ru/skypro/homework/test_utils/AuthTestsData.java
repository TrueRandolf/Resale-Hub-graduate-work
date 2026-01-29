package ru.skypro.homework.test_utils;

import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;

public class AuthTestsData {

    public static final String DEFAULT_USERNAME = "user@name";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String INVALID_PASSWORD = "pss";
    public static final String VALID_PHONE = "+79991234567";
    public static final String INVALID_PHONE = "+7  1234)67";

    public static Login createLoginDto() {
        Login login = new Login();
        login.setUsername(DEFAULT_USERNAME);
        login.setPassword(DEFAULT_PASSWORD);
        return login;
    }

    public static Register createRegisterDto() {
        Register register = new Register();
        register.setUsername("user_jan");
        register.setPassword("pass8888");
        register.setFirstName("Ivan");
        register.setLastName("Ivanov");
        register.setPhone(VALID_PHONE);
        register.setRole(Role.USER);
        return register;
    }
}

