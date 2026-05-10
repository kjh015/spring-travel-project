package com.traveler.web.domain.post.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.post.dto.request.CommentRequest;
import com.traveler.web.domain.post.dto.response.CommentResponse;
import com.traveler.web.domain.post.facade.CommentFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "댓글 관련 API (작성, 수정, 삭제)")
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentFacade commentFacade;

    @Operation(summary = "댓글 작성", description = "게시글에 새로운 댓글과 별점을 남깁니다.")
    @PostMapping
    public ApiResponse<CommentResponse.CreateDTO> createComment(@Valid @RequestBody CommentRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.CREATED, commentFacade.createComment(dto));
    }

    @Operation(summary = "댓글 수정", description = "본인이 작성한 댓글의 내용 및 별점을 수정합니다.")
    @PatchMapping("/{commentId}")
    public ApiResponse<CommentResponse.UpdateDTO> updateComment(
            @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentFacade.updateComment(commentId, dto));
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentResponse.DeleteDTO> deleteComment(
            @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId) {
        return ApiResponse.onSuccess(SuccessCode.OK, commentFacade.deleteComment(commentId));
    }
}
