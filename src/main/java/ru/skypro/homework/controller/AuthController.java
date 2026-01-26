package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exceptions.UnauthorizedException;
import ru.skypro.homework.service.AuthService;

import javax.validation.Valid;

/**
 * Контроллер для работы с авторизацией пользователя.
 * <p>Обеспечивает:</p>
 * <ul>
 *     <li> регистрацию нового пользователя </li>
 *     <li> авторизацию зарегистрированного пользователя </li>
 * </ul>
 *
 * @see ru.skypro.homework.service.AuthService
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Tag(name = "Авторизация")
    @Operation(summary = "Авторизация пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            })
    @PostMapping("/login")
    public void login(@Valid @RequestBody Login login) {
        if (!authService.login(login.getUsername(), login.getPassword())) {
            throw new UnauthorizedException("");
        }
        log.info("Successful login user {}", login.getUsername());
    }

    @Tag(name = "Регистрация")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация пользователя",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content()),
            })
    @PostMapping("/register")
    public void register(@Valid @RequestBody Register register) {
        authService.register(register);
        log.info("User registered {}", register.getUsername());
    }
}
