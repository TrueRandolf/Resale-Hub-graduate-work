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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.support.AdsTestData;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Объявления")
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
    public Ads getAllAds() {
        return AdsTestData.createFullAds();
    }

    @PostMapping(value = "/ads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Добавление объявления",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public Ad addAd(
            @RequestPart("properties")
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            //@Parameter(schema = @Schema(type = "object", description = ""))
            //@io.swagger.v3.oas.annotations.Parameter(content = @io.swagger.v3.oas.annotations.media.Content(mediaType = org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
            @Valid CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image,
            Authentication authentication) {
        if (properties == null || image == null || image.getOriginalFilename().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Ad ad = AdsTestData.createEmptyAd();
        ad.setPk(500);
        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setImage(image.getOriginalFilename());

        return ad;
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
    public ExtendedAd getAds(@PathVariable Integer id) {

        return AdsTestData.createFullExtendedAd();
    }

    @DeleteMapping("/ads/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удаление объявления",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    public void removeAd(@PathVariable Integer id) {
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
    public Ad updateAds(
            @PathVariable Integer id,
            @RequestBody(required = false) CreateOrUpdateAd update) {
        if (update == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Ad updateAd = AdsTestData.createFullAd();
        updateAd.setPk(id);
        updateAd.setTitle(update.getTitle());
        updateAd.setPrice(update.getPrice());

        return updateAd;
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
    public Ads getAdsMe(Authentication authentication) {
        Ads ads = AdsTestData.createFullAds();
        return ads;
    }


    @PatchMapping(value = "/ads/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(
            summary = "Обновление картинки объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,//"application/octet-stream",
                                    array = @ArraySchema(schema = @Schema(type = "string", format = "byte"))
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    //public ResponseEntity<byte[]> updateImage(@PathVariable("id") Integer id,
    public byte[] updateImage(@PathVariable("id") Integer id,
                              @RequestPart(value = "image") MultipartFile image) {
        if (image.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        byte[] imageData;
        try {
            imageData = image.getBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(imageData);
        return imageData;
    }


}
