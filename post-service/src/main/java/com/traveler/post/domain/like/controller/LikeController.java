package com.traveler.post.domain.like.controller;

import com.traveler.common.api.auth.LoginUser;
import com.traveler.common.api.auth.UserContext;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.like.dto.req.LikeReqDTO;
import com.traveler.post.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ApiResponse<Void> addLike(@RequestBody LikeReqDTO.AddDTO dto, @LoginUser UserContext user) {
        likeService.addLike(dto, user.id());
        return ApiResponse.onSuccess(SuccessCode.CREATED, null);
    }

    @DeleteMapping
    public ApiResponse<Void> removeLike(@RequestParam Long postId, @LoginUser UserContext user) {
        likeService.removeLike(postId, user.id());
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }
}
