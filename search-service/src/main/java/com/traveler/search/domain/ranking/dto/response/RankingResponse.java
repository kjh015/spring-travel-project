package com.traveler.search.domain.ranking.dto.response;

import java.util.List;

public class RankingResponse {

    public record PostRank(Long postId, Long memberId, String title, String thumbnail, Long score) {}

    public record CategoryRank(String category, Long score) {}

    public record RegionRank(String region, Long score) {}

    public record LiveDTO(List<PostRank> posts, List<CategoryRank> categories, List<RegionRank> regions) {}
}
