package com.traveler.search.domain.ranking.mapper;

import com.traveler.search.domain.post.document.PostDocument;
import com.traveler.search.domain.post.repository.PostDocumentRepository;
import com.traveler.search.domain.ranking.dto.response.RankingResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RankingMapper {

    RankingResponse.LiveDTO toLiveDTO(
            List<RankingResponse.PostRank> posts,
            List<RankingResponse.CategoryRank> categories,
            List<RankingResponse.RegionRank> regions);

    @Mapping(source = "id", target = "postId")
    @Mapping(source = "popularityScoreValue", target = "score")
    @Mapping(
            target = "thumbnail",
            expression = "java(post.getImages().isEmpty() ? null : post.getImages().get(0).getImageKey())")
    RankingResponse.PostRank toPostRank(PostDocument post);

    List<RankingResponse.CategoryRank> toCategoryRankList(List<PostDocumentRepository.RankingResult> results);

    @Mapping(source = "name", target = "category")
    RankingResponse.CategoryRank toCategoryRank(PostDocumentRepository.RankingResult result);

    List<RankingResponse.RegionRank> toRegionRankList(List<PostDocumentRepository.RankingResult> results);

    @Mapping(source = "name", target = "region")
    RankingResponse.RegionRank toRegionRank(PostDocumentRepository.RankingResult result);
}
