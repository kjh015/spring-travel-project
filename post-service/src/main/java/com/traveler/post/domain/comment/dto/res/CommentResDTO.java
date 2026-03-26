package com.traveler.post.domain.comment.dto.res;

import java.time.LocalDateTime;

public class CommentResDTO {

    public record CreateDTO(
            Long commentId,
            LocalDateTime createdAt
    ){}

    public record UpdateDTO(
            Long commentId,
            LocalDateTime updatedAt
    ){}

    public record DeleteDTO(
            Long commentId,
            LocalDateTime deletedAt
    ){}
}
