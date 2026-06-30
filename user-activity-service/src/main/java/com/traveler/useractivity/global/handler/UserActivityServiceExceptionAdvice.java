package com.traveler.useractivity.global.handler;

import com.traveler.common.api.handler.BaseExceptionAdvice;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.traveler.useractivity")
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class UserActivityServiceExceptionAdvice implements BaseExceptionAdvice {

    /** Member Service에서 발생하는 커스텀 비즈니스 예외 처리 */
    @ExceptionHandler(UserActivityServiceException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMemberServiceException(UserActivityServiceException ex) {
        if (ex.getCause() != null) {
            log.error(
                    "[UserActivity Service System Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage(), ex);
        } else {
            log.warn("[UserActivity Service Business Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage());
        }
        return createErrorResponse(ex.getCode(), null);
    }
}
