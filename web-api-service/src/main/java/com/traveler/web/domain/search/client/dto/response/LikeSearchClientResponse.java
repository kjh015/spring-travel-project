package com.traveler.web.domain.search.client.dto.response;

import java.time.Instant;

public final class LikeSearchClientResponse {

    private LikeSearchClientResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

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
            Instant updatedAt) {}
}
