package com.traveler.web.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

public final class AdminPostResponse {

    private AdminPostResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(description = "웹 어드민 게시글 목록 조회 응답 DTO")
    public record ListDTO(
            @Schema(description = "게시글 ID", example = "101") Long postId,
            @Schema(description = "작성자 회원 ID", example = "1") Long memberId,
            @Schema(description = "제목", example = "제주도 여행 후기") String title,
            @Schema(description = "조회수", example = "150") Long viewCount,
            @Schema(description = "댓글 수", example = "10") Long commentCount,
            @Schema(description = "좋아요 수", example = "25") Long likeCount,
            @Schema(description = "평균 별점", example = "4.5") Double starAvg,
            @Schema(description = "작성 일시") Instant createdAt,
            @Schema(description = "삭제 여부", example = "false") boolean isDeleted,
            @Schema(description = "삭제 일시 (미삭제 시 null)") Instant deletedAt) {}

    @Schema(description = "웹 어드민 게시글 상세 조회 응답 DTO")
    public record DetailDTO(
            @Schema(description = "게시글 ID", example = "101") Long postId,
            @Schema(description = "작성자 회원 ID", example = "1") Long memberId,
            @Schema(description = "제목", example = "제주도 여행 후기") String title,
            @Schema(description = "본문", example = "제주도 다녀왔습니다...") String content,
            @Schema(description = "카테고리", example = "NATURE") String category,
            @Schema(description = "지역", example = "JEJU") String region,
            @Schema(description = "여행지명", example = "성산일출봉") String travelPlace,
            @Schema(description = "주소", example = "제주특별자치도 서귀포시") String address,
            @Schema(description = "조회수", example = "150") Long viewCount,
            @Schema(description = "댓글 수", example = "10") Long commentCount,
            @Schema(description = "좋아요 수", example = "25") Long likeCount,
            @Schema(description = "평균 별점", example = "4.5") Double starAvg,
            @Schema(description = "이미지 키 목록 (정렬 순서 순)") List<String> images,
            @Schema(description = "작성 일시") Instant createdAt,
            @Schema(description = "수정 일시") Instant updatedAt,
            @Schema(description = "삭제 여부", example = "true") boolean isDeleted,
            @Schema(description = "삭제 일시 (미삭제 시 null)") Instant deletedAt) {}

    @Schema(description = "웹 어드민 게시글 강제 삭제 응답 DTO")
    public record DeleteDTO(
            @Schema(description = "강제 삭제된 게시글 ID", example = "101") Long postId,
            @Schema(description = "삭제 일시") Instant deletedAt) {}

    @Schema(description = "웹 어드민 게시글 복구 응답 DTO")
    public record RestoreDTO(
            @Schema(description = "복구된 게시글 ID", example = "101") Long postId,
            @Schema(description = "제목", example = "제주도 여행 후기") String title) {}

    @Schema(description = "웹 어드민 게시글 영구 삭제 응답 DTO")
    public record PermanentDeleteDTO(@Schema(description = "영구 삭제된 게시글 ID", example = "101") Long postId) {}
}
