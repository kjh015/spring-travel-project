package com.traveler.search.domain.like.controller;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.search.domain.like.dto.response.LikeSearchResponse;
import com.traveler.search.domain.like.service.LikeSearchQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search/likes")
public class LikeSearchController {
    private final LikeSearchQueryService likeSearchQueryService;

    @GetMapping("/me")
    public ApiResponse<PageResponse<LikeSearchResponse.MyDTO>> getMyLikePosts(
            @LoginUser UserContext user,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, likeSearchQueryService.getMyLikePosts(user.id(), pageable));
    }
}
