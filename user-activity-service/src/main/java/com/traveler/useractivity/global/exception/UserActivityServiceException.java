package com.traveler.useractivity.global.exception;

import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.exception.GeneralException;

public class UserActivityServiceException extends GeneralException {
    public UserActivityServiceException(BaseErrorCode code) {
        super(code);
    }

    public UserActivityServiceException(BaseErrorCode code, Throwable cause) {
        super(code, cause);
    }
}
