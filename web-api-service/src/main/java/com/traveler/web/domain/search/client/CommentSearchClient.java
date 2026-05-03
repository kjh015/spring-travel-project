package com.traveler.web.domain.search.client;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.search.client.dto.response.CommentSearchClientResponse;
import com.traveler.web.global.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "search-service", path = "/v1/search/comments", configuration = FeignClientConfig.class)
public interface CommentSearchClient {
    @GetMapping
    ApiResponse<PageResponse<CommentSearchClientResponse.ListDTO>> getComments(
            @RequestParam Long postId, Pageable pageable);

    @GetMapping("/me")
    ApiResponse<PageResponse<CommentSearchClientResponse.MyDTO>> getMyComments(Pageable pageable);
}
