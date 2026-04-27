package com.traveler.post.domain.like.controller;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.like.dto.request.LikeRequest;
import com.traveler.post.domain.like.service.LikeService;
import com.traveler.post.global.code.PostServiceErrorCode;
import com.traveler.post.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "좋아요 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "좋아요 등록", description = "특정 게시글에 좋아요를 추가합니다. 이미 좋아요를 누른 상태에서 재요청 시 추가 처리없이 성공 응답을 반환합니다.")
    @ApiErrorCodeExamples(post = {PostServiceErrorCode.POST_NOT_FOUND})
    @PostMapping
    public ApiResponse<Void> addLike(@RequestBody LikeRequest.AddDTO dto, @LoginUser UserContext user) {
        likeService.addLike(dto, user.id());
        return ApiResponse.onSuccess(SuccessCode.CREATED, null);
    }

    @Operation(summary = "좋아요 취소", description = "특정 게시글에 누른 좋아요를 취소합니다. 좋아요 기록이 없으면 아무 동작도 수행하지 않습니다.")
    @ApiErrorCodeExamples(post = {PostServiceErrorCode.POST_NOT_FOUND})
    @DeleteMapping
    public ApiResponse<Void> removeLike(@RequestParam Long postId, @LoginUser UserContext user) {
        likeService.removeLike(postId, user.id());
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }
}
