package ru.skypro.homework.test_utils;

import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommentsTestsData {
    public static final Integer START_ID = 1;
    public static final String DEFAULT_TEXT = "Default comment text (long enough)";
    public static final String UPDATED_TEXT = "Updated comment text (also long)";
    public static final Long CREATED_AT = 1700000000000L;

    public static Comment createCommentDto(int id) {
        Comment comment = new Comment();
        comment.setPk(START_ID + id);
        comment.setAuthor(UserTestsData.USER_ID);
        comment.setAuthorFirstName(UserTestsData.DEFAULT_FIRST_NAME);
        comment.setAuthorImage(UserTestsData.IMAGE_PATH);
        comment.setCreatedAt(CREATED_AT);
        comment.setText(DEFAULT_TEXT);
        return comment;
    }

    public static Comments createCommentsDto(int count) {
        List<Comment> list = IntStream.range(0, count)
                .mapToObj(CommentsTestsData::createCommentDto)
                .collect(Collectors.toList());

        Comments comments = new Comments();
        comments.setCount(count);
        comments.setResults(list);
        return comments;
    }

    public static CreateOrUpdateComment createUpdateCommentDto() {
        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText(UPDATED_TEXT);
        return dto;
    }
}
