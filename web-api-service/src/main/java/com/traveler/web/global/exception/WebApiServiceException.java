package com.traveler.web.global.exception;

import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.exception.GeneralException;

public class WebApiServiceException extends GeneralException {
    public WebApiServiceException(BaseErrorCode code) {
        super(code);
    }

    public WebApiServiceException(BaseErrorCode code, Throwable cause) {
        super(code, cause);
    }
}
