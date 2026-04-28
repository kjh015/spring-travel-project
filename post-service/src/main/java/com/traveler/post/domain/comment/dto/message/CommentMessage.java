package com.traveler.post.domain.comment.dto.message;

import java.time.Instant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMessage {

    public record CreatedDTO(
            Long commentId, Long postId, Long memberId, String content, Integer star, Instant createdAt) {}

    public record UpdatedDTO(Long commentId, String content, Integer star, Instant updatedAt) {}

    public record DeletedDTO(Long commentId) {}
}
