package com.traveler.web.domain.search.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.search.dto.request.PostSearchRequest;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import com.traveler.web.domain.search.facade.PostSearchFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search/posts")
@RequiredArgsConstructor
public class PostSearchController {
    private final PostSearchFacade postSearchFacade;

    @Operation(summary = "게시글 통합 검색", description = "키워드, 카테고리, 지역 필터를 통해 게시글을 검색합니다. 정렬 조건(정확도, 최신순, 인기순 등)을 지원합니다.")
    @GetMapping
    public ApiResponse<PageResponse<PostSearchResponse.ListDTO>> search(
            @ParameterObject @Valid @ModelAttribute PostSearchRequest.SearchDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, postSearchFacade.search(dto));
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글의 상세 정보를 조회합니다.")
    @GetMapping("/{postId}")
    public ApiResponse<PostSearchResponse.DetailDTO> getPost(
            @Parameter(description = "게시글 ID", example = "101") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, postSearchFacade.getPostDetail(postId));
    }

    @Operation(summary = "검색어 자동완성", description = "입력 중인 키워드에 대해 게시글 제목 기반의 자동완성 목록을 제공합니다. 초성 검색을 지원합니다.")
    @GetMapping("/autocomplete")
    public ApiResponse<PostSearchResponse.AutocompleteDTO> autocomplete(
            @Parameter(description = "검색 키워드", example = "경주")
                    @RequestParam
                    @NotBlank(message = "검색어는 공백일 수 없습니다.")
                    @Size(min = 1, message = "최소 1자 이상 입력해야 합니다.")
                    String keyword) {
        return ApiResponse.onSuccess(SuccessCode.OK, postSearchFacade.autocomplete(keyword));
    }

    @Operation(summary = "내 게시글 검색", description = "내가 작성한 게시글을 검색 엔진 인덱스에서 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<PageResponse<PostSearchResponse.ListDTO>> getMyPosts(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, postSearchFacade.getMyPosts(pageable));
    }
}
