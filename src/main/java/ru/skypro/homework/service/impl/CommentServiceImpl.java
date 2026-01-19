package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.support.CommentsTestData;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
//    Comments getAllCommentsAd(Long adId);
//
//    Comment addCommentToAd(Long adId);
//
//    boolean deleteComment(Long adId, Long commentId);
//
//    Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment comment);

    public Comments getAllCommentsAd(Long adId) {
        log.info("invoked comment service get all comments");
        return CommentsTestData.createFullComments();
    }

    public Comment addCommentToAd(Long adId, CreateOrUpdateComment updateComment) {
        log.info("invoked comment service add comment");

        Comment comment = CommentsTestData.createFullComment();
        comment.setText(updateComment.getText());
        return comment;
        //return CommentsTestData.createFullComment();
    }

    public boolean deleteComment(Long adId, Long commentId) {
        log.info("invoked comment service delete comment");
        return true;
    }

    public Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment updateComment) {
        log.info("invoked comment service update comment");

        if (updateComment == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Comment comment = CommentsTestData.createFullComment();
        comment.setText(updateComment.getText());

        return comment;

    }

    ;

}
