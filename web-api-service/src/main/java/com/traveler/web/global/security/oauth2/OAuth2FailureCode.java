package com.traveler.web.global.security.oauth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2FailureCode {
    ACCESS_DENIED("access_denied", "사용자가 동의를 취소했습니다."),
    PROVIDER_SERVER_ERROR("provider_server_error", "인증 서버와 통신 중 오류가 발생했습니다."),
    SESSION_EXPIRED("session_expired", "세션이 만료되었거나 비정상적인 요청입니다."),
    AUTHENTICATION_FAILED("authentication_failed", "인증에 실패했습니다."),
    SYSTEM_ERROR("system_error", "시스템 내부 인증 처리 중 오류가 발생했습니다.");

    private final String errorCode;
    private final String description;

    /**
     * Spring Security의 에러 코드를 프론트엔드용 Enum으로 변환
     */
    public static OAuth2FailureCode from(String springErrorCode) {
        if (springErrorCode == null) {
            return AUTHENTICATION_FAILED;
        }

        return switch (springErrorCode) {
            case "access_denied" -> ACCESS_DENIED;
            case "invalid_token_response", "server_error" -> PROVIDER_SERVER_ERROR;
            case "authorization_request_not_found" -> SESSION_EXPIRED;
            default -> AUTHENTICATION_FAILED;
        };
    }
}
