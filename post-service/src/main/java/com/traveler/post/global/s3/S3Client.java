package com.traveler.post.global.s3;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.post.global.exception.PostServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Client {

    private final software.amazon.awssdk.services.s3.S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

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

        try {
            s3Client.deleteObjects(multiObjectDeleteRequest);
            log.info("Successfully deleted {} keys from S3", keys.size());
        } catch (Exception e) {
            log.error("Failed to delete objects from S3. Keys: {}", keys, e);
        }
    }

    public void deleteFilesByUrls(List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty()) return;
        List<String> keys = fileUrls.stream().map(this::extractKey).toList();
        deleteFilesByKeys(keys);
    }

    private String extractKey(String fileUrl) {
        try {
            String path = java.net.URI.create(fileUrl).getPath();
            if (path.startsWith("/")) {
                path = path.substring(1); // 앞의 '/' 제거
            }
            return URLDecoder.decode(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to extract key from URL: {}", fileUrl);
            throw new PostServiceException(ErrorCode.S3_INVALID_URL);
        }
    }
}
