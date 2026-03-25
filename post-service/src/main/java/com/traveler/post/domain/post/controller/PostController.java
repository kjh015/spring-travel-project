package com.traveler.post.domain.post.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.post.dto.req.PostReqDTO;
import com.traveler.post.domain.post.dto.res.PostResDTO;
import com.traveler.post.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ApiResponse<PostResDTO.CreateDTO> createPost(@RequestBody PostReqDTO.CreateDTO dto){
        return ApiResponse.onSuccess(SuccessCode.CREATED, postService.createPost(dto));
    }

    @PatchMapping("/{postId}")
    public ApiResponse<PostResDTO.UpdateDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody PostReqDTO.UpdateDTO dto){
        return ApiResponse.onSuccess(SuccessCode.OK, postService.updatePost(postId, dto));
    }
    @DeleteMapping("/{postId}")
    public ApiResponse<PostResDTO.DeleteDTO> deletePost(@PathVariable Long postId){
        return ApiResponse.onSuccess(SuccessCode.OK, postService.deletePost(postId));
    }
}
