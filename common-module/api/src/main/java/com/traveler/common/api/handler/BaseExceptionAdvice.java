package com.traveler.common.api.handler;

import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface BaseExceptionAdvice {
    default <T> ResponseEntity<ApiResponse<T>> createErrorResponse(BaseErrorCode code, T data) {
        return ResponseEntity.status(code.getStatus()).body(ApiResponse.onFailure(code, data));
    }
}
