package com.traveler.search.domain.comment.controller;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.search.domain.comment.dto.response.CommentSearchResponse;
import com.traveler.search.domain.comment.service.CommentSearchQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comment Search", description = "댓글 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search/comments")
@Validated
public class CommentSearchController {
    private final CommentSearchQueryService commentSearchQueryService;

    @Operation(summary = "게시글별 댓글 조회", description = "특정 게시글의 댓글 목록을 검색 엔진에서 페이징하여 조회합니다.")
    @GetMapping
    public ApiResponse<PageResponse<CommentSearchResponse.ListDTO>> getComments(
            @RequestParam Long postId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentSearchQueryService.getComments(postId, pageable));
    }

    @Operation(summary = "내가 쓴 댓글 조회", description = "현재 로그인한 사용자가 작성한 모든 댓글을 검색 엔진에서 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<PageResponse<CommentSearchResponse.MyDTO>> getMyComments(
            @LoginUser UserContext user,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentSearchQueryService.getMyComments(user.id(), pageable));
    }
}
