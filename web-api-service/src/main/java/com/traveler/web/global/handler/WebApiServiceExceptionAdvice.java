package com.traveler.web.global.handler;

import com.traveler.common.api.handler.BaseExceptionAdvice;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.global.exception.WebApiServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.traveler.web")
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class WebApiServiceExceptionAdvice implements BaseExceptionAdvice {

    /** WebApi Service에서 발생하는 커스텀 비즈니스 예외 처리 */
    @ExceptionHandler(WebApiServiceException.class)
    protected ResponseEntity<ApiResponse<Void>> handleGeneralException(WebApiServiceException ex) {
        log.warn("[WebApi Service Business Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage());
        return createErrorResponse(ex.getCode(), null);
    }
}
