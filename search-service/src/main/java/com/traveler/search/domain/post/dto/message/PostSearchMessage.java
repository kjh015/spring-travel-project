package com.traveler.search.domain.post.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

public final class PostSearchMessage {

    private PostSearchMessage() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record ImageInfo(String imageKey, int sortOrder) {}

    public record CreatedDTO(
            Long postId,
            Long memberId,
            String title,
            String content,
            String category,
            String region,
            String travelPlace,
            String address,
            Instant createdAt,
            List<ImageInfo> images) {}

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // null이나 ""은 JSON 직렬화에서 제외
    public record UpdatedDTO(
            Long postId,
            String title,
            String content,
            String category,
            String region,
            String travelPlace,
            String address,
            Instant updatedAt,
            List<ImageInfo> images) {}

    public record DeletedDTO(Long postId) {}

    public record StatUpdatedDTO(Long postId, Double starAvg, Long commentCount, Long likeCount, Long viewCount) {}

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record StatUpdateDoc(
            Long postId,
            Double starAvg,
            Long commentCount,
            Long likeCount,
            Long viewCount,
            Double popularityScore,
            Double popularityScoreValue) {}
}
