package com.traveler.post.domain.comment.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.comment.dto.req.CommentReqDTO;
import com.traveler.post.domain.comment.dto.res.CommentResDTO;
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
    public ApiResponse<CommentResDTO.CreateDTO> createComment(@Valid @RequestBody CommentReqDTO.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.CREATED, commentService.createComment(dto));
    }

    @PatchMapping("/{commentId}")
    public ApiResponse<CommentResDTO.UpdateDTO> updateComment(
            @PathVariable Long commentId, @Valid @RequestBody CommentReqDTO.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentService.updateComment(commentId, dto));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentResDTO.DeleteDTO> deletePost(@PathVariable Long commentId) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentService.deleteComment(commentId));
    }
}
