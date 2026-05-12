package com.traveler.web.domain.search.client;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.search.client.dto.request.PostSearchClientRequest;
import com.traveler.web.domain.search.client.dto.response.PostSearchClientResponse;
import com.traveler.web.global.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "search-service",
        contextId = "SearchPostClient",
        path = "/v1/search/posts",
        configuration = FeignClientConfig.class)
public interface PostSearchClient {
    @GetMapping
    ApiResponse<PageResponse<PostSearchClientResponse.SearchDTO>> search(
            @SpringQueryMap PostSearchClientRequest.SearchDTO dto);

    @GetMapping("/{postId}")
    ApiResponse<PostSearchClientResponse.DetailDTO> getPost(@PathVariable Long postId);

    @GetMapping("/autocomplete")
    ApiResponse<PostSearchClientResponse.AutocompleteDTO> autocomplete(@RequestParam String keyword);

    @GetMapping("/me")
    ApiResponse<PageResponse<PostSearchClientResponse.MyDTO>> getMyPosts(@SpringQueryMap Pageable pageable);
}
