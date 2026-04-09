package com.traveler.post.domain.comment.mapper;

import com.traveler.post.domain.comment.dto.message.CommentMessage;
import com.traveler.post.domain.comment.dto.request.CommentRequest;
import com.traveler.post.domain.comment.dto.response.CommentResponse;
import com.traveler.post.domain.comment.entity.Comment;
import com.traveler.post.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "dto.content", target = "content")
    @Mapping(target = "memberId", source = "memberId")
    Comment toCreateEntity(CommentRequest.CreateDTO dto, Post post, Long memberId);

    @Mapping(source = "id", target = "commentId")
    CommentResponse.CreateDTO toCreateDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    CommentResponse.UpdateDTO toUpdateDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    CommentResponse.DeleteDTO toDeleteDTO(Comment comment);

    // Kafka
    @Mapping(source = "id", target = "commentId")
    CommentMessage.CreatedDTO toCreatedMsgDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    CommentMessage.UpdatedDTO toUpdatedMsgDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    CommentMessage.DeletedDTO toDeletedMsgDTO(Comment comment);
}
