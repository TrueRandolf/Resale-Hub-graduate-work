package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.constants.AppErrorsMessages;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.CommentEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.exceptions.NotFoundException;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.AccessService;
import ru.skypro.homework.service.CommentService;

/**
 * Реализация сервиса управления комментариями.
 *
 * <p>Обеспечивает работу с отзывами пользователей, проверяя принадлежность
 * комментария к объявлению и наличие прав доступа у автора запроса.</p>
 */

@Slf4j
@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final AccessService accessService;

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Comments getAllCommentsAd(Long adId, Authentication authentication) {
        log.info("invoked comment service get all comments");

        accessService.checkAuth(authentication);

        AdEntity adEntity = adsRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.AD_NOT_FOUND));

        return commentMapper.toComments(commentRepository.findByAd_Id(adId));
    }

    /**
     * {@inheritDoc}
     * <p>Устанавливает текущее время создания в формате Unix Timestamp
     * перед сохранением записи в БД.</p>
     */
    @Override
    @Transactional
    public Comment addCommentToAd(Long adId, CreateOrUpdateComment updateComment, Authentication authentication) {
        log.info("invoked comment service add comment");

        accessService.checkAuth(authentication);

        AdEntity adEntity = adsRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.AD_NOT_FOUND));

        UserEntity userEntity = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.USER_NOT_FOUND));

        CommentEntity commentEntity = commentMapper.toEntity(updateComment);
        commentEntity.setUser(userEntity);
        commentEntity.setAd(adEntity);
        commentEntity.setCreatedAt(System.currentTimeMillis());
        commentRepository.save(commentEntity);
        return commentMapper.toCommentDto(commentEntity);

    }

    /**
     * {@inheritDoc}
     * <p>Помимо прав доступа, проверяет корректность связи между объявлением
     * и комментарием для предотвращения ошибок адресации.</p>
     */
    @Override
    @Transactional
    public void deleteComment(Long adId, Long commentId, Authentication authentication) {
        log.info("invoked comment service delete comment");

        accessService.checkAuth(authentication);

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.COMMENT_NOT_FOUND));

        if (!adsRepository.existsById(adId)) {
            throw new NotFoundException(AppErrorsMessages.AD_NOT_FOUND);
        }

        if (!commentEntity.getAd().getId().equals(adId)) {
            throw new NotFoundException(AppErrorsMessages.INVALID_RELATION);
        }

        accessService.checkEdit(authentication, commentEntity.getUser().getUserName());

        commentRepository.delete(commentEntity);
    }

    /**
     * {@inheritDoc}
     * <p>Выполняет обновление текста существующего комментария при условии
     * подтверждения прав владения контентом.</p>
     */
    @Override
    @Transactional
    public Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment updateComment, Authentication authentication) {
        log.info("invoked comment service update comment");

        accessService.checkAuth(authentication);

        AdEntity adEntity = adsRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.AD_NOT_FOUND));

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(AppErrorsMessages.COMMENT_NOT_FOUND));

        if (!commentEntity.getAd().getId().equals(adId)) {
            throw new NotFoundException(AppErrorsMessages.INVALID_RELATION);
        }

        accessService.checkEdit(authentication, commentEntity.getUser().getUserName());

        commentMapper.updateCommentEntity(updateComment, commentEntity);
        commentRepository.save(commentEntity);
        return commentMapper.toCommentDto(commentEntity);
    }

}
