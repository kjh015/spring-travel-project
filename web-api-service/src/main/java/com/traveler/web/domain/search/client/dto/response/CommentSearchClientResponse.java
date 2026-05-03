package com.traveler.web.domain.search.client.dto.response;

public final class CommentSearchClientResponse {

    private CommentSearchClientResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record ListDTO(Long commentId, Long postId, Long memberId, String content, Integer star) {}

    public record MyDTO(Long commentId, Long postId, Long memberId, String content, Integer star) {}
}
