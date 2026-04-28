package com.traveler.post.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PostResponse {

    public record CreateDTO(
            @Schema(description = "생성된 게시글 ID", example = "101") Long postId,
            @Schema(description = "생성 일시") Instant createdAt) {}

    public record UpdateDTO(
            @Schema(description = "수정된 게시글 ID", example = "101") Long postId,
            @Schema(description = "수정 일시") Instant updatedAt) {}

    public record DeleteDTO(
            @Schema(description = "삭제된 게시글 ID", example = "101") Long postId,
            @Schema(description = "삭제 일시") Instant deletedAt) {}
}
