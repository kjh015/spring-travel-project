package com.traveler.web.domain.search.dto.response;

import java.util.List;

public final class RankingResponse {
    private RankingResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record PostRank(Long postId, Long memberId, String title, String thumbnail, Long score) {}

    public record CategoryRank(String category, Long score) {}

    public record RegionRank(String region, Long score) {}

    public record LiveDTO(List<PostRank> posts, List<CategoryRank> categories, List<RegionRank> regions) {}
}
