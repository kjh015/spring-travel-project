package com.traveler.post.global.handler;

import com.traveler.common.api.handler.BaseExceptionAdvice;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.global.exception.PostServiceException;
import com.traveler.post.global.exception.code.PostServiceErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.services.s3.model.S3Exception;

@RestControllerAdvice(basePackages = "com.traveler.post")
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class PostServiceExceptionAdvice implements BaseExceptionAdvice {

    /** AWS S3 관련 SDK 예외 처리 */
    @ExceptionHandler(S3Exception.class)
    protected ResponseEntity<ApiResponse<Void>> handleS3Exception(S3Exception ex) {
        String errorMessage = Optional.ofNullable(ex.awsErrorDetails())
                .map(AwsErrorDetails::errorMessage)
                .orElseGet(ex::getMessage);
        String errorCode = Optional.ofNullable(ex.awsErrorDetails())
                .map(AwsErrorDetails::errorCode)
                .orElse("UNKNOWN");
        log.error("[S3 Error] Status: {}, Message: {}", ex.statusCode(), errorMessage);

        PostServiceErrorCode mappedErrorCode =
                switch (errorCode) {
                    case "NoSuchKey" -> PostServiceErrorCode.S3_FILE_NOT_FOUND;
                    case "AccessDenied" -> PostServiceErrorCode.S3_ACCESS_DENIED;
                    case "InvalidRequest", "InvalidArgument" -> PostServiceErrorCode.S3_INVALID_URL;
                    default -> PostServiceErrorCode.S3_OPERATION_ERROR;
                };

        return createErrorResponse(mappedErrorCode, null);
    }

    /** Post Service에서 발생하는 커스텀 비즈니스 예외 처리 */
    @ExceptionHandler(PostServiceException.class)
    protected ResponseEntity<ApiResponse<Void>> handleGeneralException(PostServiceException ex) {
        if (ex.getCause() != null) {
            log.error("[Post Service System Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage(), ex);
        } else {
            log.warn("[Post Service Business Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage());
        }
        return createErrorResponse(ex.getCode(), null);
    }
}
