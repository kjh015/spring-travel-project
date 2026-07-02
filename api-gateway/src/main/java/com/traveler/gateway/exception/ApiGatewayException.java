package com.traveler.gateway.exception;

import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.exception.GeneralException;

public class ApiGatewayException extends GeneralException {
    public ApiGatewayException(BaseErrorCode code, Throwable cause) {
        super(code, cause);
    }

    public ApiGatewayException(BaseErrorCode code) {
        super(code);
    }
}
