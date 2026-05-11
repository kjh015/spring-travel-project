package com.traveler.post.domain.post.controller;

import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.auth.UserContext;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.post.dto.response.PostImageResponse;
import com.traveler.post.domain.post.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/posts/images")
@RequiredArgsConstructor
public class PostImageController {
    private final PostImageService postImageService;

    @GetMapping("/presigned-url")
    public ApiResponse<PostImageResponse.PresignedUrlDTO> getPresignedUrl(
            @RequestParam String fileName, @RequestParam String contentType, @LoginUser UserContext user) {
        return ApiResponse.onSuccess(
                SuccessCode.OK, postImageService.getPresignedUrl(user.id(), fileName, contentType));
    }
}
