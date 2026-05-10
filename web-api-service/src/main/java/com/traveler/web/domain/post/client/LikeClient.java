package com.traveler.web.domain.post.client;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.post.client.dto.request.LikeClientRequest;
import com.traveler.web.global.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "post-service",
        contextId = "LikeClient",
        path = "/v1/likes",
        configuration = FeignClientConfig.class)
public interface LikeClient {
    @PostMapping
    ApiResponse<Void> addLike(@RequestBody LikeClientRequest.AddDTO dto);

    @DeleteMapping
    ApiResponse<Void> removeLike(@RequestParam Long postId);
}
