package com.traveler.post.domain.comment.dto.request;

public class CommentRequest {

    public record CreateDTO(Long postId, String content, Integer star) {}

    public record UpdateDTO(String content, Integer star) {}
}
