package com.traveler.post.domain.comment.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.post.domain.comment.dto.response.AdminCommentResponse;
import com.traveler.post.domain.comment.service.AdminCommentService;
import com.traveler.post.global.exception.code.PostServiceErrorCode;
import com.traveler.post.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Comment", description = "어드민 전용 댓글 관리 API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/v1/admin/comments")
public class AdminCommentController {
    private final AdminCommentService adminCommentService;

    @Operation(summary = "전체 댓글 목록 조회", description = "게시글 ID/삭제 여부 필터를 포함하여 전체 댓글을 페이징 조회합니다.")
    @GetMapping
    public ApiResponse<PageResponse<AdminCommentResponse.ListDTO>> getComments(
            @Parameter(description = "게시글 ID 필터 (미지정 시 전체)") @RequestParam(required = false) Long postId,
            @Parameter(description = "삭제 여부 필터 (true: 삭제된 댓글만, false: 활성 댓글만, 미지정: 전체)") @RequestParam(required = false)
                    Boolean deleted,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminCommentService.getComments(postId, deleted, pageable));
    }

    @Operation(summary = "댓글 강제 삭제", description = "작성자 확인 없이 댓글을 삭제(Soft Delete) 처리하고 게시글 통계를 갱신합니다.")
    @ApiErrorCodeExamples(
            post = {
                PostServiceErrorCode.COMMENT_NOT_FOUND,
                PostServiceErrorCode.COMMENT_ALREADY_DELETED,
                PostServiceErrorCode.POST_NOT_FOUND
            })
    @DeleteMapping("/{commentId}")
    public ApiResponse<AdminCommentResponse.DeleteDTO> deleteComment(
            @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminCommentService.deleteComment(commentId));
    }

    @Operation(summary = "댓글 복구", description = "삭제(Soft Delete)된 댓글을 복구합니다. 부모 게시물이 삭제된 경우 복구할 수 없습니다.")
    @ApiErrorCodeExamples(
            post = {
                PostServiceErrorCode.COMMENT_NOT_FOUND,
                PostServiceErrorCode.COMMENT_NOT_DELETED,
                PostServiceErrorCode.COMMENT_PARENT_POST_DELETED,
                PostServiceErrorCode.POST_NOT_FOUND
            })
    @PatchMapping("/{commentId}/restore")
    public ApiResponse<AdminCommentResponse.RestoreDTO> restoreComment(
            @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminCommentService.restoreComment(commentId));
    }
}
