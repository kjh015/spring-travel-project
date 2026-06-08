package com.traveler.web.domain.post.client.dto.response;

import java.time.Instant;

public final class CommentClientResponse {

    private CommentClientResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(Long commentId, Instant createdAt) {}

    public record UpdateDTO(Long commentId, Instant updatedAt) {}

    public record DeleteDTO(Long commentId, Instant deletedAt) {}
}
