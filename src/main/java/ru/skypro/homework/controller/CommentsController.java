package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("/ads/{id}/comments")
    @Operation(
            summary = "Получение комментариев объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Comments.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content())
            }
    )
    public Comments getComments(@PathVariable Integer id, Authentication authentication) {
        return commentService.getAllCommentsAd(Long.valueOf(id), authentication);
    }

    @PostMapping("/ads/{id}/comments")
    @Operation(
            summary = "Добавление комментария к объявлению",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Comment.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content())
            }
    )
    public Comment addComment(
            @PathVariable Integer id,
            @Valid @RequestBody(required = false) CreateOrUpdateComment updateComment,
            Authentication authentication

    ) {
        return commentService.addCommentToAd(Long.valueOf(id), updateComment, authentication);
    }


    @DeleteMapping("/ads/{adId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No content", content = @Content()),
                    //@ApiResponse(responseCode = "200", description = "OK", content = @Content()),
                    // Что писать-то??? в ТЗ одно, в схеме другое, скайпро, короче...
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    public void deleteComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            Authentication authentication) {
        commentService.deleteComment(Long.valueOf(adId), Long.valueOf(commentId), authentication);
    }

    @Operation(
            summary = "Обновление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Comment.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    @PatchMapping("/ads/{adId}/comments/{commentId}")
    public Comment updateComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            @Valid @RequestBody(required = false) CreateOrUpdateComment commentUpdate,
            Authentication authentication
    ) {

        return commentService.updateComment(Long.valueOf(adId), Long.valueOf(commentId), commentUpdate, authentication);
    }


}
