package com.traveler.web.global.code;

import com.traveler.common.core.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WebApiServiceErrorCode implements BaseErrorCode {
    // FeignClient 통신 중 발생하는 에러
    SERVICE_RESPONSE_EMPTY(500, "WEB500_1", "하위 서비스가 응답 바디 없이 에러를 반환했습니다."),
    SERVICE_PARSE_ERROR(500, "WEB500_2", "하위 서비스의 에러 응답을 분석하는 데 실패했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
