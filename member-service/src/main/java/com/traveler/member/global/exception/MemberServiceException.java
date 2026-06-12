package com.traveler.member.global.exception;

import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.exception.GeneralException;

public class MemberServiceException extends GeneralException {
    public MemberServiceException(BaseErrorCode code) {
        super(code);
    }
}
