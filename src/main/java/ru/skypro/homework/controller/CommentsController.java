package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.support.CommentsTestData;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentsController {

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
    public Comments getComments(@PathVariable Integer id) {

        return CommentsTestData.createFullComments();
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
            @RequestBody(required = false) CreateOrUpdateComment updateComment
    ) {
        if (updateComment == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Comment comment = CommentsTestData.createFullComment();
        comment.setText(updateComment.getText());
        return comment;
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
            @PathVariable Integer commentId) {

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
            @RequestBody(required = false) CreateOrUpdateComment commentUpdate
    ) {
        if (commentUpdate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Comment comment = CommentsTestData.createFullComment();
        comment.setText(commentUpdate.getText());

        return comment;
    }


}
