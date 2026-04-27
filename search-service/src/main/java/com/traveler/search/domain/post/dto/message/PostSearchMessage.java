package com.traveler.search.domain.post.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PostSearchMessage {

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
            List<ImageInfo> images) {}

    public record DeletedDTO(Long postId, LocalDateTime deletedAt) {}

    public record StatUpdatedDTO(Long postId, Double starAvg, Long commentCount, Long likeCount, Long viewCount) {}

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record StatUpdateDoc(
            Long postId, Double starAvg, Long commentCount, Long likeCount, Long viewCount, Double popularityScore) {}
}
