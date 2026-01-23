package ru.skypro.homework.security;

import org.springframework.security.core.Authentication;

public interface AccessService {

    void checkAuth(Authentication authentication);

    void checkEdit(Authentication authentication, String username);

    void checkAdmin(Authentication authentication);
}
