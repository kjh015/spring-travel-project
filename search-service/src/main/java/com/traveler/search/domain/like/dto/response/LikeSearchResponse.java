package com.traveler.search.domain.like.dto.response;

import java.time.LocalDateTime;

public class LikeSearchResponse {
    public record MyDTO(
            Long postId,
            Long memberId,
            String title,
            String category,
            String region,
            Double starAvg,
            Long viewCount,
            Long likeCount,
            Long commentCount,
            LocalDateTime updatedAt) {}
}
