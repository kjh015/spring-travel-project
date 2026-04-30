package com.traveler.common.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseSuccessCode {
    OK(200, "COMMON200_1", "요청이 정상적으로 처리되었습니다."),
    CREATED(201, "COMMON201_1", "리소스가 성공적으로 생성되었습니다."),
    ACCEPTED(202, "COMMON202_1", "요청이 접수되었습니다. 처리에 시간이 소요될 수 있습니다.");

    private final int status;
    private final String code;
    private final String message;
}
