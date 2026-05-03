package com.traveler.web.domain.search.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import com.traveler.web.domain.search.facade.LikeSearchFacade;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search/likes")
@RequiredArgsConstructor
public class LikeSearchController {
    private final LikeSearchFacade likeSearchFacade;

    @Operation(summary = "내가 좋아요 한 게시글 목록 조회", description = "현재 사용자가 좋아요를 누른 게시글들을 검색 엔진 인덱스 조인을 통해 페이징하여 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<PageResponse<PostSearchResponse.ListDTO>> getMyLikePosts(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, likeSearchFacade.getMyLikePosts(pageable));
    }
}
