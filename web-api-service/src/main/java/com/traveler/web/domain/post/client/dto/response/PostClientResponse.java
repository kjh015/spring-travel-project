package com.traveler.web.domain.post.client.dto.response;

import java.time.Instant;

public final class PostClientResponse {

    private PostClientResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(Long postId, Instant createdAt) {}

    public record UpdateDTO(Long postId, Instant updatedAt) {}

    public record DeleteDTO(Long postId, Instant deletedAt) {}
}
