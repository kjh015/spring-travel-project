package com.traveler.post.domain.like.controller;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.like.dto.request.LikeRequest;
import com.traveler.post.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ApiResponse<Void> addLike(@RequestBody LikeRequest.AddDTO dto, @LoginUser UserContext user) {
        likeService.addLike(dto, user.id());
        return ApiResponse.onSuccess(SuccessCode.CREATED, null);
    }

    @DeleteMapping
    public ApiResponse<Void> removeLike(@RequestParam Long postId, @LoginUser UserContext user) {
        likeService.removeLike(postId, user.id());
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }
}
