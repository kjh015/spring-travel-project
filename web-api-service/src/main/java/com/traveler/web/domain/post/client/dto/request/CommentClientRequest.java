package com.traveler.web.domain.post.client.dto.request;

public final class CommentClientRequest {

    private CommentClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(Long postId, String content, Integer star) {}

    public record UpdateDTO(String content, Integer star) {}
}
