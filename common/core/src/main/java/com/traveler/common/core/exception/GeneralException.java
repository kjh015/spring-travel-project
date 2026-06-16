package com.traveler.common.core.exception;

import com.traveler.common.core.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final BaseErrorCode code;

    public GeneralException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public GeneralException(BaseErrorCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.code = code;
    }
}
