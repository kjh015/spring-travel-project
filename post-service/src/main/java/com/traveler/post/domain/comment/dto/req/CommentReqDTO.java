package com.traveler.post.domain.comment.dto.req;

public class CommentReqDTO {

    public record CreateDTO(Long postId, String content, Integer star) {}

    public record UpdateDTO(String content, Integer star) {}
}
