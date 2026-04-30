package com.traveler.post.domain.comment.controller;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.comment.dto.request.CommentRequest;
import com.traveler.post.domain.comment.dto.response.CommentResponse;
import com.traveler.post.domain.comment.service.CommentService;
import com.traveler.post.global.code.PostServiceErrorCode;
import com.traveler.post.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "댓글 관련 API (작성, 수정, 삭제)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "게시글에 새로운 댓글과 별점을 남깁니다.")
    @ApiErrorCodeExamples(post = {PostServiceErrorCode.POST_NOT_FOUND})
    @PostMapping
    public ApiResponse<CommentResponse.CreateDTO> createComment(
            @Valid @RequestBody CommentRequest.CreateDTO dto, @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.CREATED, commentService.createComment(user.id(), dto));
    }

    @Operation(summary = "댓글 수정", description = "본인이 작성한 댓글의 내용 및 별점을 수정합니다.")
    @ApiErrorCodeExamples(
            value = {ErrorCode.FORBIDDEN},
            post = {
                PostServiceErrorCode.COMMENT_NOT_FOUND,
                PostServiceErrorCode.POST_NOT_FOUND,
                PostServiceErrorCode.COMMENT_ALREADY_DELETED
            })
    @PatchMapping("/{commentId}")
    public ApiResponse<CommentResponse.UpdateDTO> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest.UpdateDTO dto,
            @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentService.updateComment(commentId, user.id(), dto));
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiErrorCodeExamples(
            value = {ErrorCode.FORBIDDEN},
            post = {PostServiceErrorCode.COMMENT_NOT_FOUND, PostServiceErrorCode.POST_NOT_FOUND})
    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentResponse.DeleteDTO> deleteComment(
            @PathVariable Long commentId, @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentService.deleteComment(commentId, user.id()));
    }
}
