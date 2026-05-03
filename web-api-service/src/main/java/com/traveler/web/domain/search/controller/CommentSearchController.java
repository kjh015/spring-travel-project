package com.traveler.web.domain.search.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.search.dto.response.CommentSearchResponse;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import com.traveler.web.domain.search.facade.CommentSearchFacade;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search/comments")
@RequiredArgsConstructor
public class CommentSearchController {
    private final CommentSearchFacade commentSearchFacade;

    @Operation(summary = "게시글별 댓글 조회", description = "특정 게시글의 댓글 목록을 검색 엔진에서 페이징하여 조회합니다.")
    @GetMapping
    public ApiResponse<PageResponse<CommentSearchResponse.ListDTO>> getComments(
            @RequestParam Long postId,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentSearchFacade.getComments(postId, pageable));
    }

    @Operation(summary = "내가 쓴 댓글 조회", description = "현재 로그인한 사용자가 작성한 모든 댓글을 검색 엔진에서 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<PageResponse<PostSearchResponse.ListDTO>> getMyComments(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentSearchFacade.getMyComments(pageable));
    }
}
