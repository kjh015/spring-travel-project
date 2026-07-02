package com.traveler.search.global.exception;

import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.exception.GeneralException;

public class SearchServiceException extends GeneralException {
    public SearchServiceException(BaseErrorCode code) {
        super(code);
    }

    public SearchServiceException(BaseErrorCode code, Throwable cause) {
        super(code, cause);
    }
}
