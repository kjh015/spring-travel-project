package com.traveler.post.domain.comment.controller;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.comment.dto.request.CommentRequest;
import com.traveler.post.domain.comment.dto.response.CommentResponse;
import com.traveler.post.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ApiResponse<CommentResponse.CreateDTO> createComment(
            @Valid @RequestBody CommentRequest.CreateDTO dto, @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.CREATED, commentService.createComment(user.id(), dto));
    }

    @PatchMapping("/{commentId}")
    public ApiResponse<CommentResponse.UpdateDTO> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest.UpdateDTO dto,
            @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentService.updateComment(commentId, user.id(), dto));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentResponse.DeleteDTO> deleteComment(
            @PathVariable Long commentId, @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentService.deleteComment(user.id(), commentId));
    }
}
