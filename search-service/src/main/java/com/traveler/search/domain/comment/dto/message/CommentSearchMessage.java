package com.traveler.search.domain.comment.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

public class CommentSearchMessage {
    public record CreatedDTO(Long commentId, Long postId, Long memberId, String content, Integer star) {}

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record UpdatedDTO(Long commentId, String content, Integer star) {}

    public record DeletedDTO(Long commentId, LocalDateTime deletedAt) {}
}
