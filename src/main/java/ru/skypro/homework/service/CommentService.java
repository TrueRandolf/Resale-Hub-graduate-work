package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;

/**
 * Сервис для управления комментариями к объявлениям.
 *
 * <p>Реализует бизнес-логику создания, изменения и удаления комментариев.
 * Все операции проводятся с привязкой к идентификатору объявления.</p>
 */

public interface CommentService {

    /**
     * Получение списка всех комментариев конкретного объявления.
     * @param adId идентификатор объявления.
     * @param authentication данные пользователя из контекста безопасности.
     */
    Comments getAllCommentsAd(Long adId, Authentication authentication);

    /**
     * Добавление комментария к объявлению.
     * @param adId идентификатор объявления.
     * @param updateComment текст комментария.
     * @param authentication данные пользователя из контекста безопасности.
     */
    Comment addCommentToAd(Long adId, CreateOrUpdateComment updateComment, Authentication authentication);

    /**
     * Удаление комментария к объявлению.
     * @param adId идентификатор объявления.
     * @param commentId идентификатор комментария.
     * @param authentication данные пользователя из контекста безопасности.
     */
    void deleteComment(Long adId, Long commentId, Authentication authentication);

    /**
     * Обновление комментария к объявлению.
     * @param adId идентификатор объявления.
     * @param commentId идентификатор комментария.
     * @param comment текст комментария.
     * @param authentication данные пользователя из контекста безопасности.
     */
    Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment comment, Authentication authentication);
}
