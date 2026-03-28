package com.traveler.post.domain.post.controller;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.post.dto.req.PostReqDTO;
import com.traveler.post.domain.post.dto.res.PostResDTO;
import com.traveler.post.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ApiResponse<PostResDTO.CreateDTO> createPost(
            @Valid @RequestBody PostReqDTO.CreateDTO dto, @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.CREATED, postService.createPost(user.id(), dto));
    }

    @PatchMapping("/{postId}")
    public ApiResponse<PostResDTO.UpdateDTO> updatePost(
            @PathVariable Long postId, @Valid @RequestBody PostReqDTO.UpdateDTO dto, @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, postService.updatePost(postId, user.id(), dto));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<PostResDTO.DeleteDTO> deletePost(@PathVariable Long postId, @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, postService.deletePost(postId, user.id()));
    }
}
