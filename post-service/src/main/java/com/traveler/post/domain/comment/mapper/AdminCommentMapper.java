package com.traveler.post.domain.comment.mapper;

import com.traveler.post.domain.comment.dto.response.AdminCommentResponse;
import com.traveler.post.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminCommentMapper {

    @Mapping(source = "id", target = "commentId")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "deleted", target = "isDeleted")
    AdminCommentResponse.ListDTO toListDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    AdminCommentResponse.DeleteDTO toDeleteDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    @Mapping(source = "post.id", target = "postId")
    AdminCommentResponse.RestoreDTO toRestoreDTO(Comment comment);
}
