package com.traveler.search.domain.post.controller;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.search.domain.post.dto.request.PostSearchRequest;
import com.traveler.search.domain.post.dto.response.PostSearchResponse;
import com.traveler.search.domain.post.service.PostSearchQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search/posts")
public class PostSearchController {
    private final PostSearchQueryService postSearchQueryService;

    @GetMapping
    public ApiResponse<PageResponse<PostSearchResponse.SearchDTO>> search(
            @ModelAttribute PostSearchRequest.SearchDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, postSearchQueryService.search(dto));
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostSearchResponse.DetailDTO> getPost(@PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, postSearchQueryService.getPostDetail(postId));
    }

    @GetMapping("/autocomplete")
    public ApiResponse<PostSearchResponse.AutocompleteDTO> autocomplete(@RequestParam String keyword) {
        return ApiResponse.onSuccess(SuccessCode.OK, postSearchQueryService.autocomplete(keyword));
    }

    @GetMapping("/me")
    public ApiResponse<PageResponse<PostSearchResponse.MyDTO>> getMyPosts(
            @LoginUser UserContext user,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, postSearchQueryService.getMyPosts(user.id(), pageable));
    }
}
