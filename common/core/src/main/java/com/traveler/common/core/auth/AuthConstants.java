package com.traveler.common.core.auth;

public final class AuthConstants {
    private AuthConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // HTTP Header 관련
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // Gateway -> Microservice 전달용 내부 보안 헤더
    public static final String X_USER_ID = "X-User-Id";
    public static final String X_USER_ROLES = "X-User-Roles";

    // Redis 관련 키 접두사
    public static final String REDIS_BLACKLIST_PREFIX = "blacklist:";

    // JWT Claims Key 관련
    public static final String CLAIM_ROLES = "roles";
}
