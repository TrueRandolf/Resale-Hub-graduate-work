package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.users.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UsersController {

    @PostMapping("/users/set_password")
    @Operation(
            summary = "Обновление пароля",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            }
    )
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPassword newPassword) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/me")
    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public ResponseEntity<User> getUser(Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/me")
    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUser.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public ResponseEntity<UpdateUser> updateUser(@Valid @RequestBody(required = false) UpdateUser updateUser) {
        if(updateUser == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        UpdateUser fakeUser = new UpdateUser();
        fakeUser.setFirstName(updateUser.getFirstName());
        fakeUser.setLastName(updateUser.getLastName());
        fakeUser.setPhone(updateUser.getPhone());

        return ResponseEntity.ok(fakeUser);
    }

    @PatchMapping(value = "/users/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public ResponseEntity<Void> updateUserImage(@RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok().build();
    }
}
