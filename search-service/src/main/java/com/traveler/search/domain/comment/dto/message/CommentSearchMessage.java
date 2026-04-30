package com.traveler.search.domain.comment.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentSearchMessage {
    public record CreatedDTO(
            Long commentId, Long postId, Long memberId, String content, Integer star, Instant createdAt) {}

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record UpdatedDTO(Long commentId, String content, Integer star, Instant updatedAt) {}

    public record DeletedDTO(Long commentId) {}
}
