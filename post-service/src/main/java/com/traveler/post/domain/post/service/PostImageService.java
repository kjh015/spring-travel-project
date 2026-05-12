package com.traveler.post.domain.post.service;

import com.traveler.post.domain.post.dto.response.PostImageResponse;
import com.traveler.post.domain.post.mapper.PostImageMapper;
import com.traveler.post.global.code.PostServiceErrorCode;
import com.traveler.post.global.exception.PostServiceException;
import com.traveler.post.global.s3.S3Service;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostImageService {
    private final S3Service s3Service;
    private final PostImageMapper postImageMapper;

    private static final Map<String, Set<String>> ALLOWED_FILE_TYPES = Map.of(
            "jpg", Set.of("image/jpeg"),
            "jpeg", Set.of("image/jpeg"),
            "png", Set.of("image/png"),
            "webp", Set.of("image/webp"));

    public PostImageResponse.PresignedUrlDTO getPresignedUrl(Long memberId, String fileName, String contentType) {
        validateFile(fileName, contentType);
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String key = String.format("posts/%d/%s.%s", memberId, UUID.randomUUID(), extension);

        String url = s3Service.generatePresignedUrl(key, contentType);

        return postImageMapper.toPresignedUrlDTO(url, key);
    }

    private void validateFile(String fileName, String contentType) {
        if (fileName == null || fileName.isBlank()) {
            throw new PostServiceException(PostServiceErrorCode.S3_INVALID_FILE_EXTENSION);
        }
        if (contentType == null || contentType.isBlank()) {
            throw new PostServiceException(PostServiceErrorCode.S3_INVALID_FILE_TYPE);
        }

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new PostServiceException(PostServiceErrorCode.S3_INVALID_FILE_EXTENSION);
        }

        String extension = fileName.substring(dotIndex + 1).toLowerCase();
        Set<String> allowedMimeTypes = ALLOWED_FILE_TYPES.get(extension);
        if (allowedMimeTypes == null) {
            throw new PostServiceException(PostServiceErrorCode.S3_INVALID_FILE_EXTENSION);
        }
        if (!allowedMimeTypes.contains(contentType)) {
            throw new PostServiceException(PostServiceErrorCode.S3_INVALID_FILE_TYPE);
        }
    }
}
