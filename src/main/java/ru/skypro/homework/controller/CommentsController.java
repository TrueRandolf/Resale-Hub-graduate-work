package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;

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
    public ResponseEntity<Comments> getComments(@PathVariable Integer id) {

        return ResponseEntity.ok().build();
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
    public ResponseEntity<Comment> addComment(
            @PathVariable Integer id,
            @RequestBody(required = false) CreateOrUpdateComment updateComment
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/ads/{adId}/comments/{commentId}")
    @Operation(
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content()),
            }
    )
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId) {
        return ResponseEntity.ok().build();
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


    public ResponseEntity<Comment> updateComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            @RequestBody(required = false) CreateOrUpdateComment commentUpdate
    ) {
        return ResponseEntity.ok().build();
    }


}
