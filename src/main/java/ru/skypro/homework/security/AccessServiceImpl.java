package ru.skypro.homework.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.constants.AppErrorsMessages;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exceptions.ForbiddenException;
import ru.skypro.homework.exceptions.UnauthorizedException;

/**
 * Реализация сервиса контроля доступа.
 *
 * <p>Методы выбрасывают {@link UnauthorizedException} для неавторизованных пользователей
 * и {@link ForbiddenException} при нарушении прав доступа.
 * </p>Анонимные токены считаются неавторизованными.</p>
 */

@Slf4j
@AllArgsConstructor
@Service
public class AccessServiceImpl implements AccessService {

    public void checkAuth(Authentication authentication) {
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            throw new UnauthorizedException(AppErrorsMessages.ACCESS_DENIED);
        }
    }

    public void checkEdit(Authentication authentication, String username) {
        checkAuth(authentication);
        if (!(authentication.getName().equals(username) || isAdmin(authentication))) {
            throw new ForbiddenException(AppErrorsMessages.ACCESS_DENIED);
        }
    }

    public void checkAdmin(Authentication authentication) {
        if (!isAdmin(authentication)) {
            log.warn("Trying non-admin access to admin-only operation! {}", authentication.getName());
            throw new ForbiddenException(AppErrorsMessages.ONLY_ADMIN_ACCESS);
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.getRole()));
    }
}
