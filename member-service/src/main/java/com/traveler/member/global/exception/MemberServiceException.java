package com.traveler.member.global.exception;

import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.exception.GeneralException;

public class MemberServiceException extends GeneralException {
    public MemberServiceException(BaseErrorCode code) {
        super(code);
    }

    public MemberServiceException(BaseErrorCode code, Throwable cause) {
        super(code, cause); // 상위 클래스(GeneralException -> RuntimeException)로 cause 전달
    }
}
