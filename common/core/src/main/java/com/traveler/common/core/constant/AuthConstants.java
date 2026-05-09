package com.traveler.common.core.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지
public final class AuthConstants {

    // 1. HTTP Header 관련
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // 2. Gateway -> Microservice 전달용 내부 보안 헤더
    public static final String X_USER_ID = "X-User-Id";
    public static final String X_USER_ROLES = "X-User-Roles";

    // 3. Redis 관련 키 접두사
    public static final String REDIS_BLACKLIST_PREFIX = "blacklist:";

    // 4. JWT Claims Key 관련
    public static final String CLAIM_ROLES = "roles";
}
