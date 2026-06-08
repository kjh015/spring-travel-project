package com.traveler.gateway.exception;

import com.traveler.common.core.code.BaseErrorCode;
import java.util.Objects;

public class ApiGatewayNoStackException extends RuntimeException {
    private final BaseErrorCode code;

    public ApiGatewayNoStackException(BaseErrorCode code) {
        // message, cause, enableSuppression, writableStackTrace 순서
        super(Objects.requireNonNull(code, "BaseErrorCode must not be null").getMessage(), null, false, false);
        this.code = code;
    }

    public BaseErrorCode getCode() {
        return code;
    }
}
