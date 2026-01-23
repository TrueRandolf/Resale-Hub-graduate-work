package ru.skypro.homework.mappers;

import org.mapstruct.*;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entities.CommentEntity;
import ru.skypro.homework.entities.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
//
//    @Mapping(target = "author", source = "user.id")
//    @Mapping(target = "authorImage", source = "user.userImage")
//    @Mapping(target = "authorFirstname", source = "user.firstName")
//    @Mapping(target = "pk", source = "id")
//    Comment toCommentDto(CommentEntity comment);

    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "authorImage", source = "user", qualifiedByName = "mapAvatar")
    @Mapping(target = "authorFirstname", source = "user", qualifiedByName = "mapName")
    @Mapping(target = "pk", source = "id")
    Comment toCommentDto(CommentEntity comment);

    @Named("mapName")
    default String mapName(UserEntity user) {
        if (user.getDeletedAt() != null) {
            return "deleted user";
        }
        return user.getFirstName();
    }

    @Named("mapAvatar")
    default String mapAvatar(UserEntity user) {
        if (user.getDeletedAt() != null) {
            return null;
        }
        return user.getUserImage();
    }


    List<Comment> toCommentList(List<CommentEntity> commentEntities);

    default Comments toComments(List<CommentEntity> commentEntities) {
        Comments comments = new Comments();
        comments.setCount(commentEntities.size());
        comments.setResults(toCommentList(commentEntities));
        return comments;
    }

    void updateCommentEntity(CreateOrUpdateComment dto, @MappingTarget CommentEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommentEntity toEntity(CreateOrUpdateComment createOrUpdateComment);

}
