package com.traveler.web.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public final class PostResponse {

    private PostResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(
            @Schema(description = "생성된 게시글 ID", example = "101") Long postId,
            @Schema(description = "생성 일시") Instant createdAt) {}

    public record UpdateDTO(
            @Schema(description = "수정된 게시글 ID", example = "101") Long postId,
            @Schema(description = "수정 일시") Instant updatedAt) {}

    public record DeleteDTO(
            @Schema(description = "삭제된 게시글 ID", example = "101") Long postId,
            @Schema(description = "삭제 일시") Instant deletedAt) {}

    @Schema(name = "PostPresignedUrlResponse", description = "Presigned URL 발급 응답")
    public record PresignedUrlDTO(
            @Schema(
                            description = "S3 업로드용 URL",
                            example = "https://travel-bucket.s3.ap-northeast-2.amazonaws.com/posts/...")
                    String url,
            @Schema(
                            description = "S3 객체 키 (게시글 생성/수정 API 요청 시 images 리스트에 담아 전송)",
                            example = "images/2026/05/11/a1b2c3d4-e5f6.png")
                    String imageKey) {}
}
