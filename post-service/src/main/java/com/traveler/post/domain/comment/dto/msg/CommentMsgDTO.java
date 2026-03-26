package com.traveler.post.domain.comment.dto.msg;

import java.time.LocalDateTime;

public class CommentMsgDTO {

    public record CreatedMessage(Long commentId, Long memberId, String content, Integer star) {}

    public record UpdatedMessage(Long commentId, String content, Integer star) {}

    public record DeletedMessage(Long commentId, LocalDateTime deletedAt) {}
}
