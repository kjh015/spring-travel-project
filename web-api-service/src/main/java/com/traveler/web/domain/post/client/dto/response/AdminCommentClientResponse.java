package com.traveler.web.domain.post.client.dto.response;

import java.time.Instant;

public final class AdminCommentClientResponse {

    private AdminCommentClientResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record ListDTO(
            Long commentId,
            Long postId,
            Long memberId,
            String content,
            Integer star,
            Instant createdAt,
            boolean isDeleted,
            Instant deletedAt) {}

    public record DeleteDTO(Long commentId, Instant deletedAt) {}

    public record RestoreDTO(Long commentId, Long postId) {}
}
