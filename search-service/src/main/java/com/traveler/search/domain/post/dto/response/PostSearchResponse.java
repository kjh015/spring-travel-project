package com.traveler.search.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class PostSearchResponse {
    public record ImageInfo(String imageKey, int sortOrder) {}

    public record SearchDTO(
            Long postId,
            Long memberId,
            String title,
            String category,
            String region,
            Double starAvg,
            Long viewCount,
            Long likeCount,
            Long commentCount,
            LocalDateTime updatedDate) {}

    public record DetailDTO(
            Long postId,
            Long memberId,
            String title,
            String content,
            String travelPlace,
            String address,
            String category,
            String region,
            Double starAvg,
            Long viewCount,
            Long likeCount,
            Long commentCount,
            LocalDateTime updatedDate,
            List<ImageInfo> images) {}

    public record AutocompleteDTO(List<String> titles) {}

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
            LocalDateTime updatedDate) {}
}
