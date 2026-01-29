package ru.skypro.homework.dto.comments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Форма создания или редактирования комментария.
 * <p> Маппинг:
 * {@link ru.skypro.homework.mappers.CommentMapper} </p>
 */

@Schema(description = "CreateOrUpdateComment")
@Data
public class CreateOrUpdateComment {

    @Schema(description = "текст комментария", minLength = 8, maxLength = 64, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 8, max = 64)
    private String text;
}
