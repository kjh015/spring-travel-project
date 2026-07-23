package com.traveler.post.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public final class AdminCommentResponse {

    private AdminCommentResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record ListDTO(
            @Schema(description = "댓글 ID", example = "11") Long commentId,
            @Schema(description = "게시글 ID", example = "101") Long postId,
            @Schema(description = "작성자 회원 ID", example = "1") Long memberId,
            @Schema(description = "내용", example = "정말 좋은 곳이네요!") String content,
            @Schema(description = "별점", example = "5") Integer star,
            @Schema(description = "작성 일시") Instant createdAt,
            @Schema(description = "삭제 여부", example = "false") boolean isDeleted,
            @Schema(description = "삭제 일시 (미삭제 시 null)") Instant deletedAt) {}

    public record DeleteDTO(
            @Schema(description = "강제 삭제된 댓글 ID", example = "11") Long commentId,
            @Schema(description = "삭제 일시") Instant deletedAt) {}

    public record RestoreDTO(
            @Schema(description = "복구된 댓글 ID", example = "11") Long commentId,
            @Schema(description = "게시글 ID", example = "101") Long postId) {}
}
