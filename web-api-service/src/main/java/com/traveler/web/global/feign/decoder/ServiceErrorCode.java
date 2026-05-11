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
        return new ServiceErrorCode(
                status, code != null ? code : "SERVICE" + status, message != null ? message : "하위 서비스에서 에러가 발생했습니다.");
    }
}
