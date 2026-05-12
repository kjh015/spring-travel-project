package com.traveler.web.global.feign.decoder;

import com.traveler.common.core.code.BaseErrorCode;

public record ServiceErrorCode(int status, String code, String message) implements BaseErrorCode {

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static ServiceErrorCode of(int status, String code, String message) {
        String normalizedCode = (code == null || code.isBlank()) ? "SERVICE" + status : code;
        String normalizedMessage = (message == null || message.isBlank()) ? "하위 서비스에서 에러가 발생했습니다." : message;
        return new ServiceErrorCode(status, normalizedCode, normalizedMessage);
    }
}
