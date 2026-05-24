package com.traveler.member.domain.auth.support;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class AuthCookieProvider {

    private final long refreshTokenExpireTime;

    public AuthCookieProvider(@Value("${app.jwt.refresh-expiration}") long refreshTokenExpireTime) {
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(refreshTokenExpireTime))
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie createLogoutCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // 즉시 만료
                .sameSite("Strict")
                .build();
    }
}
