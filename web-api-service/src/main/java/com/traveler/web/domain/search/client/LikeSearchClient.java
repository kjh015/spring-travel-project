package com.traveler.web.domain.search.client;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.search.client.dto.response.LikeSearchClientResponse;
import com.traveler.web.global.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "search-service",
        contextId = "SearchLikeClient",
        path = "/v1/search/likes",
        configuration = FeignClientConfig.class)
public interface LikeSearchClient {

    @GetMapping("/me")
    ApiResponse<PageResponse<LikeSearchClientResponse.MyDTO>> getMyLikePosts(Pageable pageable);
}
