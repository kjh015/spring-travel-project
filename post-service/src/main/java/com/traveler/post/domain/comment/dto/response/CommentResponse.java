package com.traveler.post.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentResponse {

    public record CreateDTO(
            @Schema(description = "생성된 댓글 ID", example = "501") Long commentId,
            @Schema(description = "작성 일시") LocalDateTime createdAt) {}

    public record UpdateDTO(
            @Schema(description = "수정된 댓글 ID", example = "501") Long commentId,
            @Schema(description = "수정 일시") LocalDateTime updatedAt) {}

    public record DeleteDTO(
            @Schema(description = "삭제된 댓글 ID", example = "501") Long commentId,
            @Schema(description = "삭제 일시") LocalDateTime deletedAt) {}
}
