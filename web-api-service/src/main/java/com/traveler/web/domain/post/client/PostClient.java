package com.traveler.web.domain.post.client;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.post.client.dto.request.PostClientRequest;
import com.traveler.web.domain.post.client.dto.response.PostClientResponse;
import com.traveler.web.global.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "post-service",
        contextId = "PostClient",
        path = "/v1/posts",
        configuration = FeignClientConfig.class)
public interface PostClient {
    @PostMapping
    ApiResponse<PostClientResponse.CreateDTO> createPost(@RequestBody PostClientRequest.CreateDTO dto);

    @PatchMapping("/{postId}")
    ApiResponse<PostClientResponse.UpdateDTO> updatePost(
            @PathVariable Long postId, @RequestBody PostClientRequest.UpdateDTO dto);

    @DeleteMapping("/{postId}")
    ApiResponse<PostClientResponse.DeleteDTO> deletePost(@PathVariable Long postId);
}
