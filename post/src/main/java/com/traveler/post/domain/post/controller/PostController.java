package com.traveler.post.domain.post.controller;

import com.traveler.common.api.swagger.annotation.ApiErrorCodeExamples;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.post.dto.req.PostReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    @GetMapping
    @ApiErrorCodeExamples({ErrorCode.BAD_REQUEST})
    public ApiResponse<?> test(){
        return ApiResponse.onSuccess(SuccessCode.OK, "success");
    }

    @PostMapping
    @ApiErrorCodeExamples({ErrorCode.BAD_REQUEST})
    public ApiResponse<?> createPost(@RequestBody PostReqDTO dto){
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @PatchMapping
    public ApiResponse<?> updatePost(@RequestBody PostReqDTO dto){
        return null;
    }

    @DeleteMapping
    public ApiResponse<?> deletePost(@RequestBody PostReqDTO dto){
        return null;
    }


}
