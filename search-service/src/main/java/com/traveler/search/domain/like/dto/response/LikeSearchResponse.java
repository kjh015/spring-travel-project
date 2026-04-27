package com.traveler.search.domain.like.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LikeSearchResponse {

    @Schema(description = "내가 좋아요 한 게시글 정보")
    public record MyDTO(
            @Schema(description = "게시글 식별자", example = "101") Long postId,
            @Schema(description = "작성자 식별자", example = "202") Long memberId,
            @Schema(description = "게시글 제목", example = "내가 좋아한 여행지") String title,
            @Schema(description = "카테고리", example = "ACTIVITY") String category,
            @Schema(description = "지역", example = "BUSAN") String region,
            @Schema(description = "평균 별점", example = "4.8") Double starAvg,
            @Schema(description = "조회수", example = "300") Long viewCount,
            @Schema(description = "좋아요 수", example = "150") Long likeCount,
            @Schema(description = "댓글 수", example = "25") Long commentCount,
            @Schema(description = "게시글 최종 수정 일시", example = "2026-04-27T19:41:00") LocalDateTime updatedAt) {}
}
