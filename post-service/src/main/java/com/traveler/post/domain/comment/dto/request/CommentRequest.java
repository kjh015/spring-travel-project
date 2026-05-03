package com.traveler.post.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public final class CommentRequest {

    private CommentRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(name = "CommentCreateRequest")
    public record CreateDTO(
            @NotNull(message = "게시글 ID는 필수입니다.") @Schema(description = "대상 게시글 ID", example = "101") Long postId,
            @NotBlank(message = "댓글 내용은 필수이며 공백일 수 없습니다.")
                    @Size(max = 500, message = "댓글은 500자 이내로 작성해야 합니다.")
                    @Schema(description = "댓글 내용", example = "정보 감사합니다! 도움이 많이 되었어요.")
                    String content,
            @NotNull(message = "별점은 필수입니다.")
                    @Min(value = 1, message = "별점은 최소 1점 이상이어야 합니다.")
                    @Max(value = 5, message = "별점은 최대 5점 이하여야 합니다.")
                    @Schema(description = "별점 (1~5)", example = "5")
                    Integer star) {}

    @Schema(name = "CommentUpdateRequest")
    public record UpdateDTO(
            @NotBlank(message = "수정할 댓글 내용은 필수입니다.")
                    @Size(max = 500, message = "댓글은 500자 이내로 작성해야 합니다.")
                    @Schema(description = "수정할 댓글 내용", example = "내용을 수정했습니다.")
                    String content,
            @NotNull(message = "수정할 별점은 필수입니다.") @Min(1) @Max(5) @Schema(description = "수정할 별점 (1~5)", example = "4")
                    Integer star) {}
}
