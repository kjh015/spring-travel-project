package com.traveler.web.global.security.support;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class AuthCookieProvider {

    private final long refreshTokenExpireTime;
    private final boolean secureCookie;

    public AuthCookieProvider(
            @Value("${app.jwt.refresh-expiration}") long refreshTokenExpireTime,
            @Value("${app.cookie.secure:true}") boolean secureCookie) {
        this.refreshTokenExpireTime = refreshTokenExpireTime;
        this.secureCookie = secureCookie;
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(Duration.ofMillis(refreshTokenExpireTime))
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie createLogoutCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(0) // 즉시 만료
                .sameSite("Strict")
                .build();
    }
}
