package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name="Объявления")
public class AdsController {

    @GetMapping("/ads")
    @Operation(
            summary = "Получение всех объявлений",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Ads.class)
                            )
                    )
            }
    )
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/ads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Добавление объявления",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public ResponseEntity<Ad> addAd(
            @RequestPart("properties")
            @Parameter(schema = @Schema(type = "object",description = ""))
            @Valid CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image,
            Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ads/{id}")
    @Operation(
            summary = "Получение информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExtendedAd.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/ads/{id}")
    @Operation(
            summary = "Удаление объявления",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/ads/{id}")
    @Operation(
            summary = "Обновление информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Ad.class
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    public ResponseEntity<CreateOrUpdateAd> updateAds(
            @PathVariable Integer id,
            @RequestBody(required = false) CreateOrUpdateAd update) {
        return ResponseEntity.ok().build();
    }


    @GetMapping("/ads/me")
    @Operation(
            summary = "Получение объявлений авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Ads.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok().build();
    }


    @PatchMapping(value = "/ads/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(
            summary = "Обновление картинки объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,//"application/octet-stream",
//                                    schema = @Schema(
//                                            type = "array",
//                                            items = @Schema(type = "string",format = "byte")
//                                    )
                                    array = @ArraySchema(schema = @Schema(type = "string", format = "byte"))
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    public ResponseEntity<byte[]> updateImage(@PathVariable("id") Integer id,
                                              @RequestPart(value = "image") MultipartFile image) {
        return ResponseEntity.ok().build();
    }


}
