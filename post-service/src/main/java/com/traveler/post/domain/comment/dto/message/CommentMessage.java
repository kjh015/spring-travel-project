package com.traveler.post.domain.comment.dto.message;

import java.time.LocalDateTime;

public class CommentMessage {

    public record CreatedDTO(Long commentId, Long postId, Long memberId, String content, Integer star) {}

    public record UpdatedDTO(Long commentId, String content, Integer star) {}

    public record DeletedDTO(Long commentId, LocalDateTime deletedAt) {}
}
