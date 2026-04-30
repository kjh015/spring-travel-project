package com.traveler.post.global.exception;

import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.exception.GeneralException;

public class PostServiceException extends GeneralException {
    public PostServiceException(BaseErrorCode code) {
        super(code);
    }
}
