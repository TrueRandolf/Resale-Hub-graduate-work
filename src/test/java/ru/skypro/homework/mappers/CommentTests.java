package ru.skypro.homework.mappers;

import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.CommentEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.test_utils.TestData;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTests {
    private final CommentMapper commentMapper = org.mapstruct.factory.Mappers.getMapper(CommentMapper.class);

    @Test
    void shouldMapCommentFromCommentEntity() {
        UserEntity userEntity = TestData.createTestUserEntity();
        AdEntity adEntity = TestData.createTestAdEntity(userEntity);
        CommentEntity commentEntity = TestData.createTestCommentEntity(userEntity, adEntity);

        Comment comment = commentMapper.toCommentDto(commentEntity);

        assertThat(comment).isNotNull();
        assertThat(comment.getAuthor().longValue()).isEqualTo(userEntity.getId());
        assertThat(comment.getAuthorImage()).isEqualTo(userEntity.getUserImage());
        assertThat(comment.getAuthorFirstname()).isEqualTo(userEntity.getFirstName());
        assertThat(comment.getCreatedAt()).isEqualTo(commentEntity.getCreatedAt());
        assertThat(comment.getPk().longValue()).isEqualTo(commentEntity.getId());
        assertThat(comment.getText()).isEqualTo(commentEntity.getText());

    }

    @Test
    void shouldUpdateCommentText() {
        UserEntity userEntity = TestData.createTestUserEntity();
        AdEntity adEntity = TestData.createTestAdEntity(userEntity);
        CommentEntity commentEntity = TestData.createTestCommentEntity(userEntity, adEntity);

        String text = "This is new updated text";
        CreateOrUpdateComment comment = new CreateOrUpdateComment();
        comment.setText(text);

        commentMapper.updateCommentEntity(comment, commentEntity);

        assertThat(comment).isNotNull();
        assertThat(commentEntity.getText()).isEqualTo(text);

    }

    @Test
    void shouldMapCommentsFromListCommentEntities() {
        List<CommentEntity> commentEntities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserEntity userEntity = TestData.createTestUserEntity();
            AdEntity adEntity = TestData.createTestAdEntity(userEntity);
            CommentEntity commentEntity = TestData.createTestCommentEntity(userEntity, adEntity);
            commentEntities.add(commentEntity);
        }

        Comments comments = commentMapper.toComments(commentEntities);

        assertThat(comments).isNotNull();
        assertThat(comments.getCount()).isEqualTo(commentEntities.size());
        assertThat(comments.getResults()).isNotEmpty();
    }


}
