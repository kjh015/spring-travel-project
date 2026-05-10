package com.traveler.web.domain.search.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public final class CommentSearchResponse {

    private CommentSearchResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(description = "댓글 검색 결과 리스트 항목")
    public record ListDTO(
            @Schema(description = "댓글 ID", example = "501") Long commentId,
            @Schema(description = "게시글 ID", example = "101") Long postId,
            @Schema(description = "작성자 ID", example = "202") Long memberId,
            @Schema(description = "작성자 닉네임", example = "홍길동") String memberNickname,
            @Schema(description = "댓글 내용", example = "댓글 내용입니다.") String content,
            @Schema(description = "별점", example = "5") Integer star) {}
}
