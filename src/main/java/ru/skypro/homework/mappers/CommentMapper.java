package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entities.CommentEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "authorImage", source = "user.userImage")
    @Mapping(target = "authorFirstname", source = "user.firstName")
    @Mapping(target = "pk", source = "id")
    Comment toCommentDto(CommentEntity comment);

    List<Comment> toCommentList(List<CommentEntity> commentEntities);

    default Comments toComments(List<CommentEntity> commentEntities) {
        Comments comments = new Comments();
        comments.setCount(commentEntities.size());
        comments.setResults(toCommentList(commentEntities));
        return comments;
    }

    void updateCommentEntity(CreateOrUpdateComment dto, @MappingTarget CommentEntity entity);


}
