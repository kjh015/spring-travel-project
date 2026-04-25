package com.traveler.search.domain.comment.dto.response;

public class CommentSearchResponse {
    public record ListDTO(Long commentId, Long postId, Long memberId, String content, Integer star) {}

    public record MyDTO(Long commentId, Long postId, Long memberId, String content, Integer star) {}
}
