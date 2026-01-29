package ru.skypro.homework.dto.comments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Обертка списка форм комментариев {@link ru.skypro.homework.dto.comments.Comment} </p>.
 * <p> Маппинг:
 * {@link ru.skypro.homework.mappers.CommentMapper} </p>
 */

@Schema(description = "Comments")
@Data
public class Comments {

    @Schema(description = "общее количество комментариев")
    private Integer count;

    @Schema(description = "")
    private List<Comment> results;
}
