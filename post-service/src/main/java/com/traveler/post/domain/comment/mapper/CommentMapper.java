package com.traveler.post.domain.comment.mapper;

import com.traveler.post.domain.comment.dto.event.CommentEvent;
import com.traveler.post.domain.comment.dto.message.CommentMessage;
import com.traveler.post.domain.comment.dto.request.CommentRequest;
import com.traveler.post.domain.comment.dto.response.CommentResponse;
import com.traveler.post.domain.comment.entity.Comment;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.mapper.PostMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PostMapper.class})
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

    // Message
    @Mapping(source = "id", target = "commentId")
    CommentMessage.CreatedDTO toCreatedMsgDTO(Comment comment, Long postId);

    @Mapping(source = "id", target = "commentId")
    CommentMessage.UpdatedDTO toUpdatedMsgDTO(Comment comment);

    @Mapping(source = "id", target = "commentId")
    CommentMessage.DeletedDTO toDeletedMsgDTO(Comment comment);

    // Event
    @Mapping(target = "commentMsg", expression = "java(toCreatedMsgDTO(comment, post.getId()))") // toCreatedMsgDTO 호출
    @Mapping(target = "postMsg", source = "post") // postMapper.toUpdateStatusDTO 호출
    CommentEvent.Created toCreatedEvent(Comment comment, Post post);

    @Mapping(target = "commentMsg", source = "comment")
    @Mapping(target = "postMsg", source = "post")
    CommentEvent.Updated toUpdatedEvent(Comment comment, Post post);

    @Mapping(target = "commentMsg", source = "comment")
    @Mapping(target = "postMsg", source = "post")
    CommentEvent.Deleted toDeletedEvent(Comment comment, Post post);
}
