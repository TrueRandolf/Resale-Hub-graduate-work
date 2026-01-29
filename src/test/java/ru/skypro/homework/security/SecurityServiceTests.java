package ru.skypro.homework.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exceptions.ForbiddenException;
import ru.skypro.homework.exceptions.UnauthorizedException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTests {

    @InjectMocks
    private AccessServiceImpl accessService;

    @Mock
    private Authentication authentication;

    private final String ADMIN_MAIL = "admin@mail.com";
    private final String AUTHOR_MAIL = "author@mail.com";
    private final String OUTSIDER_MAIL = "other@mail.com";


    @Test
    @DisplayName("checkAuth: ДА для авторизованного")
    void checkAuth_Success() {
        when(authentication.isAuthenticated()).thenReturn(true);
        assertDoesNotThrow(() -> accessService.checkAuth(authentication));
    }

    @Test
    @DisplayName("checkAuth: НЕТ для null или неавторизованного")
    void checkAuth_ThrowsUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> accessService.checkAuth(null));

        when(authentication.isAuthenticated()).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> accessService.checkAuth(authentication));
    }

    @Test
    @DisplayName("checkAuth: НЕТ для анонимного токена")
    void checkAuth_ThrowsUnauthorizedForAnonymous() {
        AnonymousAuthenticationToken anonymous = mock(AnonymousAuthenticationToken.class);
        assertThrows(UnauthorizedException.class, () -> accessService.checkAuth(anonymous));
    }

    @Test
    @DisplayName("checkEdit: ДА для автора")
    void checkEdit_SuccessForAuthor() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(AUTHOR_MAIL);

        assertDoesNotThrow(() -> accessService.checkEdit(authentication, AUTHOR_MAIL));
    }

    @Test
    @DisplayName("checkEdit: ДА для админа (админ - не автор)")
    void checkEdit_SuccessForAdmin() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(ADMIN_MAIL);

        when(authentication.getAuthorities()).thenReturn((List)
                List.of(new SimpleGrantedAuthority(Role.ADMIN.getRole())));

        assertDoesNotThrow(() -> accessService.checkEdit(authentication, AUTHOR_MAIL));
    }

    @Test
    @DisplayName("checkEdit: НЕТ для не-автора и не-админа")
    void checkEdit_ThrowsForbidden() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(OUTSIDER_MAIL);
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());

        assertThrows(ForbiddenException.class, () -> accessService
                .checkEdit(authentication, AUTHOR_MAIL));
    }

    @Test
    @DisplayName("checkAdmin: ДА для админа")
    void checkAdmin_Success() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getAuthorities()).thenReturn((List)
                List.of(new SimpleGrantedAuthority(Role.ADMIN.getRole())));

        assertDoesNotThrow(() -> accessService.checkAdmin(authentication));
    }


}
