package ru.skypro.homework.dto.ads;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Обертка списка кратких форм объявлений {@link ru.skypro.homework.dto.ads.Ad} </p>.
 * <p> Маппинг:
 * {@link ru.skypro.homework.mappers.AdMapper} </p>
 */

@Schema(description = "Ads")
@Data
public class Ads {

    @Schema(description = "общее количество объявлений")
    private Integer count;

    @Schema(description = "")
    private List<Ad> results;

}
