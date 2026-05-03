package com.traveler.web.domain.post.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.post.dto.request.PostRequest;
import com.traveler.web.domain.post.dto.response.PostResponse;
import com.traveler.web.domain.post.facade.PostFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostFacade postFacade;

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 작성합니다.")
    @PostMapping
    public ApiResponse<PostResponse.CreateDTO> createPost(@Valid @RequestBody PostRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.CREATED, postFacade.createPost(dto));
    }

    @Operation(summary = "게시글 수정", description = "기존 게시글의 제목, 본문, 장소 및 이미지를 수정합니다.")
    @PatchMapping("/{postId}")
    public ApiResponse<PostResponse.UpdateDTO> updatePost(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId,
            @Valid @RequestBody PostRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, postFacade.updatePost(postId, dto));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제(Soft Delete) 처리합니다.")
    @DeleteMapping("/{postId}")
    public ApiResponse<PostResponse.DeleteDTO> deletePost(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, postFacade.deletePost(postId));
    }
}
