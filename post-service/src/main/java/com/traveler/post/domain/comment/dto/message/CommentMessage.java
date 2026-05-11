package com.traveler.post.domain.comment.dto.message;

import java.time.Instant;

public final class CommentMessage {

    private CommentMessage() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreatedDTO(
            Long commentId, Long postId, Long memberId, String content, Integer star, Instant createdAt) {}

    public record UpdatedDTO(Long commentId, String content, Integer star, Instant updatedAt) {}

    public record DeletedDTO(Long commentId) {}
}
