package com.traveler.post.domain.comment.dto.req;

public class CommentReqDTO {

    public record CreateDTO(
            Long postId,
            Long memberId,  // 리팩토링 필요
            String content,
            Integer star
    ) {}

    public record UpdateDTO(
            Long memberId,  // 리팩토링 필요
            String content,
            Integer star
    ) {}


}
