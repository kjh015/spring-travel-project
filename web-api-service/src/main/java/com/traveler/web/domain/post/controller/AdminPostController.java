package com.traveler.web.domain.post.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.post.dto.response.AdminPostResponse;
import com.traveler.web.domain.post.facade.AdminPostFacade;
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

@Tag(name = "Admin Post API (Web)", description = "웹 클라이언트 어드민 전용 게시글 관리 API (BFF)")
@Validated
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/api/v1/admin/posts")
public class AdminPostController {
    private final AdminPostFacade adminPostFacade;

    @Operation(summary = "전체 게시글 목록 조회", description = "Post 서버에 삭제 여부 필터를 포함한 전체 게시글 목록 조회를 요청합니다.")
    @GetMapping
    public ApiResponse<PageResponse<AdminPostResponse.ListDTO>> getPosts(
            @Parameter(description = "삭제 여부 필터 (true: 삭제된 게시글만, false: 활성 게시글만, 미지정: 전체)")
                    @RequestParam(required = false)
                    Boolean deleted,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminPostFacade.getPosts(deleted, pageable));
    }

    @Operation(summary = "게시글 상세 조회", description = "Post 서버에 삭제된 게시글을 포함한 상세 조회를 요청합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @GetMapping("/{postId}")
    public ApiResponse<AdminPostResponse.DetailDTO> getPost(
            @Parameter(description = "게시글 ID") @Positive(message = "게시글 ID는 양수여야 합니다.") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminPostFacade.getPost(postId));
    }

    @Operation(summary = "게시글 강제 삭제", description = "Post 서버에 게시글 강제 삭제(논리적 삭제)를 요청합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @DeleteMapping("/{postId}")
    public ApiResponse<AdminPostResponse.DeleteDTO> deletePost(
            @Parameter(description = "게시글 ID") @Positive(message = "게시글 ID는 양수여야 합니다.") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminPostFacade.deletePost(postId));
    }

    @Operation(summary = "게시글 복구", description = "Post 서버에 삭제된 게시글의 복구를 요청합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @PatchMapping("/{postId}/restore")
    public ApiResponse<AdminPostResponse.RestoreDTO> restorePost(
            @Parameter(description = "게시글 ID") @Positive(message = "게시글 ID는 양수여야 합니다.") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminPostFacade.restorePost(postId));
    }

    @Operation(summary = "게시글 영구 삭제", description = "Post 서버에 게시글 영구 삭제(물리적 삭제)를 요청합니다. 복구할 수 없습니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @DeleteMapping("/{postId}/permanent")
    public ApiResponse<AdminPostResponse.PermanentDeleteDTO> permanentlyDeletePost(
            @Parameter(description = "게시글 ID") @Positive(message = "게시글 ID는 양수여야 합니다.") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminPostFacade.permanentlyDeletePost(postId));
    }
}
