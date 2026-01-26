package ru.skypro.homework.dto.ads;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Краткая форма объявления.
 * <p> Маппинг:
 * {@link ru.skypro.homework.mappers.AdMapper} </p>
 */

@Schema(description = "Ad")
@Data
public class Ad {

    @Schema(description = "id автора объявления")
    private Integer author;

    @Schema(description = "ссылка на картинку объявления")
    private String image;

    @Schema(description = "id объявления")
    private Integer pk;

    @Schema(description = "цена объявления")
    private Integer price;

    @Schema(description = "заголовок объявления")
    private String title;
}
