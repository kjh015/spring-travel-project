package com.traveler.search.domain.ranking.dto.response;

import java.util.List;

public final class RankingResponse {
    private RankingResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record PostRank(Long postId, Long memberId, String title, String thumbnail, Double score) {}

    public record CategoryRank(String category, Double score) {}

    public record RegionRank(String region, Double score) {}

    public record LiveDTO(List<PostRank> posts, List<CategoryRank> categories, List<RegionRank> regions) {}
}
