package com.traveler.web.domain.post.client.dto.response;

import java.time.Instant;
import java.util.List;

public final class AdminPostClientResponse {

    private AdminPostClientResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record ListDTO(
            Long postId,
            Long memberId,
            String title,
            Long viewCount,
            Long commentCount,
            Long likeCount,
            Double starAvg,
            Instant createdAt,
            boolean isDeleted,
            Instant deletedAt) {}

    public record DetailDTO(
            Long postId,
            Long memberId,
            String title,
            String content,
            String category,
            String region,
            String travelPlace,
            String address,
            Long viewCount,
            Long commentCount,
            Long likeCount,
            Double starAvg,
            List<String> images,
            Instant createdAt,
            Instant updatedAt,
            boolean isDeleted,
            Instant deletedAt) {}

    public record DeleteDTO(Long postId, Instant deletedAt) {}

    public record RestoreDTO(Long postId, String title) {}

    public record PermanentDeleteDTO(Long postId) {}
}
