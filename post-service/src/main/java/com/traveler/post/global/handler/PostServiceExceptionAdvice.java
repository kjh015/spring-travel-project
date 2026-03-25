package com.traveler.post.global.handler;

import com.traveler.common.api.handler.BaseExceptionAdvice;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.global.exception.PostServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.services.s3.model.S3Exception;

@RestControllerAdvice(basePackages = "com.traveler.post")
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class PostServiceExceptionAdvice implements BaseExceptionAdvice {

    /**
     * AWS S3 관련 SDK 예외 처리
     */
    @ExceptionHandler(S3Exception.class)
    protected ResponseEntity<ApiResponse<Void>> handleS3Exception(S3Exception ex) {
        log.error("[S3 Error] Status: {}, Message: {}", ex.statusCode(), ex.awsErrorDetails().errorMessage());

        ErrorCode errorCode = switch (ex.awsErrorDetails().errorCode()) {
            case "NoSuchKey" -> ErrorCode.S3_FILE_NOT_FOUND;
            case "AccessDenied" -> ErrorCode.S3_ACCESS_DENIED;
            case "InvalidRequest", "InvalidArgument" -> ErrorCode.S3_INVALID_URL;
            default ->
                // 그 외의 경우는 일반적인 삭제/서버 에러로 처리
                    ErrorCode.S3_DELETE_ERROR;
        };

        return createErrorResponse(errorCode, null);
    }

    /**
     * Post Service에서 발생하는 커스텀 비즈니스 예외 처리
     */
    @ExceptionHandler(PostServiceException.class)
    protected ResponseEntity<ApiResponse<Void>> handleGeneralException(PostServiceException ex) {
        log.warn("[Post Service Business Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage());
        return createErrorResponse(ex.getCode(), null);
    }
}
