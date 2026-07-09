package com.traveler.web.domain.post.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.post.dto.response.AdminCommentResponse;
import com.traveler.web.domain.post.facade.AdminCommentFacade;
import com.traveler.web.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Comment API (Web)", description = "웹 클라이언트 어드민 전용 댓글 관리 API (BFF)")
@Validated
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/api/v1/admin/comments")
public class AdminCommentController {
    private final AdminCommentFacade adminCommentFacade;

    @Operation(summary = "전체 댓글 목록 조회", description = "Post 서버에 게시글 ID/삭제 여부 필터를 포함한 전체 댓글 목록 조회를 요청합니다.")
    @GetMapping
    public ApiResponse<PageResponse<AdminCommentResponse.ListDTO>> getComments(
            @Parameter(description = "게시글 ID 필터 (미지정 시 전체)") @RequestParam(required = false) Long postId,
            @Parameter(description = "삭제 여부 필터 (true: 삭제된 댓글만, false: 활성 댓글만, 미지정: 전체)") @RequestParam(required = false)
                    Boolean deleted,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminCommentFacade.getComments(postId, deleted, pageable));
    }

    @Operation(summary = "댓글 강제 삭제", description = "Post 서버에 댓글 강제 삭제(논리적 삭제)를 요청합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @DeleteMapping("/{commentId}")
    public ApiResponse<AdminCommentResponse.DeleteDTO> deleteComment(
            @Parameter(description = "댓글 ID") @Positive(message = "댓글 ID는 양수여야 합니다.") @PathVariable Long commentId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminCommentFacade.deleteComment(commentId));
    }

    @Operation(summary = "댓글 복구", description = "Post 서버에 삭제된 댓글의 복구를 요청합니다. 부모 게시물이 삭제된 경우 복구할 수 없습니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @PatchMapping("/{commentId}/restore")
    public ApiResponse<AdminCommentResponse.RestoreDTO> restoreComment(
            @Parameter(description = "댓글 ID") @Positive(message = "댓글 ID는 양수여야 합니다.") @PathVariable Long commentId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminCommentFacade.restoreComment(commentId));
    }
}
