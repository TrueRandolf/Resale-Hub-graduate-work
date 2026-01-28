package ru.skypro.homework.test_utils.support;

import lombok.experimental.UtilityClass;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommentsTestData {
    public static final int AUTHOR_ID = 101;
    public static final String AUTHOR_IMAGE = "/image/comment.jpg";
    public static final String AUTHOR_FIRST_NAME = "Anon";
    public static final long CREATED_TIME = 1740000000000L;
    public static final int COMMENT_ID = 1000;
    public static final String COMMENT_TEXT = "Test comment text";

    public static final String COMMENT_UPDATE = "Update comment text";
    public static final String COMMENT_UPDATE_SHORT = "Short";
    public static final String COMMENT_UPDATE_LONG = "TooMuchManyLongVeryLargeAndNotValidAndRepeatOnceAgainThisNotValid";

    public Comment createEmptyComment() {
        return new Comment();
    }

    public Comment createFullComment() {
        Comment comment = new Comment();
        comment.setAuthor(AUTHOR_ID);
        comment.setAuthorImage(AUTHOR_IMAGE);
        comment.setAuthorFirstName(AUTHOR_FIRST_NAME);
        comment.setCreatedAt(CREATED_TIME);
        comment.setPk(COMMENT_ID);
        comment.setText(COMMENT_TEXT);
        return comment;
    }

    public CreateOrUpdateComment createEmptyUpdateComment() {
        return new CreateOrUpdateComment();
    }

    public CreateOrUpdateComment createFullUpdateComment() {
        CreateOrUpdateComment updateComment = new CreateOrUpdateComment();
        updateComment.setText(COMMENT_UPDATE);
        return updateComment;
    }

    public CreateOrUpdateComment createFullUpdateCommentShort() {
        CreateOrUpdateComment updateComment = new CreateOrUpdateComment();
        updateComment.setText(COMMENT_UPDATE_SHORT);
        return updateComment;
    }

    public CreateOrUpdateComment createFullUpdateCommentLong() {
        CreateOrUpdateComment updateComment = new CreateOrUpdateComment();
        updateComment.setText(COMMENT_UPDATE_LONG);
        return updateComment;
    }

    public Comments createEmptyComments() {
        return new Comments();
    }

    public Comments createFullComments() {
        Comments comments = new Comments();
        List<Comment> commentList = new ArrayList<>();
        commentList.add(createFullComment());
        comments.setCount(commentList.size());
        comments.setResults(commentList);
        return comments;
    }
}
