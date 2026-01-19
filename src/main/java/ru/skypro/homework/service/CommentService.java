package ru.skypro.homework.service;

import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;

public interface CommentService {
    Comments getAllCommentsAd(Long adId);

    Comment addCommentToAd(Long adId, CreateOrUpdateComment updateComment);

    boolean deleteComment(Long adId, Long commentId);

    Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment comment);
}
