package com.traveler.web.domain.post.controller;

import com.traveler.common.api.auth.annotation.RequireAuth;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.post.dto.request.PostRequest;
import com.traveler.web.domain.post.dto.response.PostResponse;
import com.traveler.web.domain.post.facade.PostFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "게시글 관련 API (작성, 수정, 삭제)")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostFacade postFacade;

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 작성합니다.")
    @RequireAuth
    @PostMapping
    public ApiResponse<PostResponse.CreateDTO> createPost(@Valid @RequestBody PostRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.CREATED, postFacade.createPost(dto));
    }

    @Operation(summary = "게시글 수정", description = "기존 게시글의 제목, 본문, 장소 및 이미지를 수정합니다.")
    @RequireAuth
    @PatchMapping("/{postId}")
    public ApiResponse<PostResponse.UpdateDTO> updatePost(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId,
            @Valid @RequestBody PostRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, postFacade.updatePost(postId, dto));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제(Soft Delete) 처리합니다.")
    @RequireAuth
    @DeleteMapping("/{postId}")
    public ApiResponse<PostResponse.DeleteDTO> deletePost(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId) {
        return ApiResponse.onSuccess(SuccessCode.OK, postFacade.deletePost(postId));
    }

    @Operation(summary = "이미지 업로드용 Presigned URL 발급", description = "S3 이미지 업로드를 위한 presigned URL을 생성합니다.")
    @RequireAuth
    @GetMapping("/images/presigned-url")
    public ApiResponse<PostResponse.PresignedUrlDTO> getPresignedUrl(
            @Parameter(description = "파일명 (확장자 포함)", example = "photo.jpg")
                    @RequestParam
                    @NotBlank(message = "파일명은 필수입니다")
                    @Size(min = 1, max = 255, message = "파일명은 1-255자 사이여야 합니다")
                    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "파일명은 영문, 숫자, '.', '_', '-'만 허용됩니다")
                    String fileName,
            @Parameter(description = "Content-Type", example = "image/jpeg")
                    @RequestParam
                    @NotBlank(message = "Content-Type은 필수입니다")
                    @Pattern(regexp = "^image/(jpeg|png|gif|webp)$", message = "허용되지 않은 파일 형식입니다")
                    String contentType) {
        return ApiResponse.onSuccess(SuccessCode.OK, postFacade.getPresignedUrl(fileName, contentType));
    }
}
