package com.traveler.post.global.s3;

import com.traveler.post.global.exception.PostServiceException;
import com.traveler.post.global.exception.code.PostServiceErrorCode;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.presigned-url.duration-minutes:10}")
    private int presignedUrlDurationMinutes;

    private static final List<String> ALLOWED_CONTENT_TYPES =
            List.of("image/jpeg", "image/png", "image/gif", "image/webp");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final int S3_DELETE_BATCH_SIZE = 1000; // S3 API 제약

    public void deleteFilesByKeys(List<String> keys) {
        if (keys == null || keys.isEmpty()) return;

        // 1000개 단위로 파티셔닝하여 실행
        for (int i = 0; i < keys.size(); i += S3_DELETE_BATCH_SIZE) {
            List<String> subKeys = keys.subList(i, Math.min(i + S3_DELETE_BATCH_SIZE, keys.size()));
            executeDelete(subKeys);
        }
    }

    private void executeDelete(List<String> keys) {
        List<ObjectIdentifier> identifiers = keys.stream()
                .map(key -> ObjectIdentifier.builder().key(key).build())
                .toList();

        DeleteObjectsRequest multiObjectDeleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucket)
                .delete(d -> d.objects(identifiers))
                .build();

        DeleteObjectsResponse response = s3Client.deleteObjects(multiObjectDeleteRequest);

        // 부분 실패 확인
        if (response.hasErrors()) {
            List<S3Error> errors = response.errors();
            log.error("Partial failure in S3 delete. Errors: {}", errors);
            throw new PostServiceException(PostServiceErrorCode.S3_DELETE_ERROR);
        }

        log.info("Successfully deleted {} keys from S3", keys.size());
    }

    public void deleteFilesByUrls(List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty()) return;
        List<String> keys = fileUrls.stream().map(this::extractKey).toList();
        deleteFilesByKeys(keys);
    }

    private String extractKey(String fileUrl) {
        String path = java.net.URI.create(fileUrl).getPath();
        if (path == null || path.isEmpty()) {
            throw new PostServiceException(PostServiceErrorCode.S3_INVALID_URL);
        }
        return path.startsWith("/") ? path.substring(1) : path;
    }

    public String generatePresignedUrl(String key, String contentType) {
        // key 검증: 상대 경로나 특수 문자 차단
        if (key.contains("..") || key.startsWith("/")) {
            throw new PostServiceException(PostServiceErrorCode.S3_INVALID_KEY);
        }
        // contentType 검증
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new PostServiceException(PostServiceErrorCode.S3_INVALID_CONTENT_TYPE);
        }
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .contentLength(MAX_FILE_SIZE)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(presignedUrlDurationMinutes))
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }
}
