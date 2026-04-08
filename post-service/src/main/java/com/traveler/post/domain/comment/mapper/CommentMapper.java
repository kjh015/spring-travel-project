package com.traveler.post.domain.comment.mapper;

import com.traveler.post.domain.comment.dto.msg.CommentMsgDTO;
import com.traveler.post.domain.comment.dto.req.CommentReqDTO;
import com.traveler.post.domain.comment.dto.res.CommentResDTO;
import com.traveler.post.domain.comment.entity.Comment;
import com.traveler.post.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "dto.content", target = "content")
    @Mapping(target = "memberId", source = "memberId")
    Comment toCreateEntity(CommentReqDTO.CreateDTO dto, Post post, Long memberId);

    @Mapping(source = "id", target = "commentId")
    CommentResDTO.CreateDTO toCreateDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    CommentResDTO.UpdateDTO toUpdateDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    CommentResDTO.DeleteDTO toDeleteDTO(Comment comment);

    // Kafka
    @Mapping(source = "id", target = "commentId")
    CommentMsgDTO.CreatedMessage toCreatedMsgDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    CommentMsgDTO.UpdatedMessage toUpdatedMsgDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    CommentMsgDTO.DeletedMessage toDeletedMsgDTO(Comment comment);
}
