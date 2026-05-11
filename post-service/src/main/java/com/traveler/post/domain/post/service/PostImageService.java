package com.traveler.post.domain.post.service;

import com.traveler.post.domain.post.dto.response.PostImageResponse;
import com.traveler.post.domain.post.mapper.PostImageMapper;
import com.traveler.post.global.code.PostServiceErrorCode;
import com.traveler.post.global.exception.PostServiceException;
import com.traveler.post.global.s3.S3Service;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostImageService {
    private final S3Service s3Service;
    private final PostImageMapper postImageMapper;

    // 허용된 확장자 및 MIME 타입 정의
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    public PostImageResponse.PresignedUrlDTO getPresignedUrl(Long memberId, String fileName, String contentType) {
        validateFile(fileName, contentType);
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String key = String.format("posts/%d/%s.%s", memberId, UUID.randomUUID(), extension);

        String url = s3Service.generatePresignedUrl(key, contentType);

        return postImageMapper.toPresignedUrlDTO(url, key);
    }

    private void validateFile(String fileName, String contentType) {
        if (!ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new PostServiceException(PostServiceErrorCode.S3_INVALID_FILE_TYPE);
        }

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1
                || !ALLOWED_EXTENSIONS.contains(fileName.substring(dotIndex + 1).toLowerCase())) {
            throw new PostServiceException(PostServiceErrorCode.S3_INVALID_FILE_EXTENSION);
        }
    }
}
