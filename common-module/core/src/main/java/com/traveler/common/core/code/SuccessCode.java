package com.traveler.common.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseSuccessCode {
    OK(200, "COMMON200_1", "요청이 정상적으로 처리되었습니다.");

    private final int status;
    private final String code;
    private final String message;
}
