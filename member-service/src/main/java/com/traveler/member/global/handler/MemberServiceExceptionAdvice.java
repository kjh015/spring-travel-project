package com.traveler.member.global.handler;

import com.traveler.common.api.handler.BaseExceptionAdvice;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.member.global.exception.MemberServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.traveler.member")
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceExceptionAdvice implements BaseExceptionAdvice {

    /** Member Service에서 발생하는 커스텀 비즈니스 예외 처리 */
    @ExceptionHandler(MemberServiceException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMemberServiceException(MemberServiceException ex) {
        if (ex.getCause() != null) {
            log.error("[Member Service System Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage(), ex);
        } else {
            log.warn("[Member Service Business Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage());
        }
        return createErrorResponse(ex.getCode(), null);
    }
}
