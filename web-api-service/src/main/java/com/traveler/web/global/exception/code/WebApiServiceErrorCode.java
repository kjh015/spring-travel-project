package com.traveler.web.global.exception.code;

import com.traveler.common.core.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WebApiServiceErrorCode implements BaseErrorCode {
    // FeignClient 통신 중 발생하는 에러
    SERVICE_RESPONSE_EMPTY(500, "WEB500_1", "하위 서비스가 응답 바디 없이 에러를 반환했습니다."),
    SERVICE_PARSE_ERROR(500, "WEB500_2", "하위 서비스의 에러 응답을 분석하는 데 실패했습니다."),

    // JWT (Server Error)
    TOKEN_GENERATION_FAILED(500, "WEB500_3", "서버 오류로 토큰 생성 및 응답에 실패했습니다."),

    // Cookie Security (Server Error)
    COOKIE_SERIALIZE_ERROR(500, "WEB500_4", "보안 쿠키 직렬화 처리 중 서버 내부 오류가 발생했습니다."),
    COOKIE_DESERIALIZE_ERROR(500, "WEB500_5", "보안 쿠키 역직렬화 처리 중 서버 내부 오류가 발생했습니다."),

    // Oauth2 (Client Error)
    UNSUPPORTED_OAUTH_PROVIDER(400, "WEB400_1", "지원하지 않는 소셜 로그인 제공자입니다."),

    // Oauth2 (Auth Error)
    INVALID_AUTH_CODE(401, "WEB401_1", "유효하지 않거나 이미 만료된 인증 코드입니다."),
    INVALID_OAUTH_USER_INFO(401, "WEB401_2", "소셜 로그인 제공자로부터 필수 사용자 정보(ID)를 받지 못했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
