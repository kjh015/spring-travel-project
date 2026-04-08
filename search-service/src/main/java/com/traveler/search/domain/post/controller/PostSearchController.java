package com.traveler.search.domain.post.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.search.domain.post.document.PostDocument;
import com.traveler.search.domain.post.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/search")
public class PostSearchController {
    private final PostSearchService postSearchService;

    @GetMapping
    public ApiResponse<PostDocument> test(@RequestParam Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, postSearchService.test(postId));
    }
}
