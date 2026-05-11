package com.traveler.common.core.response;

import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.code.BaseSuccessCode;

public record ApiResponse<T>(boolean success, String code, String message, T result) {
    // 성공한 경우 (정적 팩토리 메서드)
    public static <T> ApiResponse<T> onSuccess(BaseSuccessCode code, T result) {
        return new ApiResponse<>(true, code.getCode(), code.getMessage(), result);
    }

    // 실패한 경우 (정적 팩토리 메서드)
    public static <T> ApiResponse<T> onFailure(BaseErrorCode code, T result) {
        return new ApiResponse<>(false, code.getCode(), code.getMessage(), result);
    }
}
