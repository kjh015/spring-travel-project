package com.traveler.gateway.exception;

import com.traveler.common.core.code.BaseErrorCode;

public class ApiGatewayNoStackException extends RuntimeException {
    private final BaseErrorCode code;

    public ApiGatewayNoStackException(BaseErrorCode code) {
        // message, cause, enableSuppression, writableStackTrace 순서
        super(code.getMessage(), null, false, false);
        this.code = code;
    }

    public BaseErrorCode getCode() {
        return code;
    }
}
