package com.traveler.search.domain.comment.controller;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.search.domain.comment.dto.response.CommentSearchResponse;
import com.traveler.search.domain.comment.service.CommentSearchQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search/comments")
public class CommentSearchController {
    private final CommentSearchQueryService commentSearchQueryService;

    @GetMapping
    public ApiResponse<PageResponse<CommentSearchResponse.ListDTO>> getComments(
            @RequestParam Long postId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentSearchQueryService.getComments(postId, pageable));
    }

    @GetMapping("/me")
    public ApiResponse<PageResponse<CommentSearchResponse.MyDTO>> getMyComments(
            @LoginUser UserContext user,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentSearchQueryService.getMyComments(user.id(), pageable));
    }
}
