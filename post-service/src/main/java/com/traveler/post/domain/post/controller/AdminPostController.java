package com.traveler.post.domain.post.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.post.domain.post.dto.response.AdminPostResponse;
import com.traveler.post.domain.post.service.AdminPostService;
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

@Tag(name = "Admin Post", description = "어드민 전용 게시글 관리 API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/v1/admin/posts")
public class AdminPostController {
    private final AdminPostService adminPostService;

    @Operation(summary = "전체 게시글 목록 조회", description = "삭제 여부 필터를 포함하여 전체 게시글을 페이징 조회합니다. deleted 미지정 시 전체 조회.")
    @GetMapping
    public ApiResponse<PageResponse<AdminPostResponse.ListDTO>> getPosts(
            @Parameter(description = "삭제 여부 필터 (true: 삭제된 게시글만, false: 활성 게시글만, 미지정: 전체)")
                    @RequestParam(required = false)
                    Boolean deleted,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminPostService.getPosts(deleted, pageable));
    }

    @Operation(summary = "게시글 상세 조회", description = "삭제된 게시글을 포함하여 게시글 상세 정보를 조회합니다.")
    @ApiErrorCodeExamples(post = {PostServiceErrorCode.POST_NOT_FOUND})
    @GetMapping("/{postId}")
    public ApiResponse<AdminPostResponse.DetailDTO> getPost(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminPostService.getPost(postId));
    }

    @Operation(summary = "게시글 강제 삭제", description = "작성자 확인 없이 게시글을 삭제(Soft Delete) 처리합니다.")
    @ApiErrorCodeExamples(post = {PostServiceErrorCode.POST_NOT_FOUND, PostServiceErrorCode.POST_ALREADY_DELETED})
    @DeleteMapping("/{postId}")
    public ApiResponse<AdminPostResponse.DeleteDTO> deletePost(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminPostService.deletePost(postId));
    }

    @Operation(summary = "게시글 복구", description = "삭제(Soft Delete)된 게시글을 복구하고 검색 문서를 재색인합니다.")
    @ApiErrorCodeExamples(post = {PostServiceErrorCode.POST_NOT_FOUND, PostServiceErrorCode.POST_NOT_DELETED})
    @PatchMapping("/{postId}/restore")
    public ApiResponse<AdminPostResponse.RestoreDTO> restorePost(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminPostService.restorePost(postId));
    }

    @Operation(summary = "게시글 영구 삭제", description = "게시글과 소속 댓글을 DB에서 완전히 삭제하고 S3 이미지 삭제 이벤트를 발행합니다. 복구할 수 없습니다.")
    @ApiErrorCodeExamples(post = {PostServiceErrorCode.POST_NOT_FOUND})
    @DeleteMapping("/{postId}/permanent")
    public ApiResponse<AdminPostResponse.PermanentDeleteDTO> permanentlyDeletePost(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminPostService.permanentlyDeletePost(postId));
    }
}
