package com.traveler.member.domain.auth.support;

import com.traveler.common.core.auth.AuthConstants;
import com.traveler.member.domain.auth.dto.AuthTokens;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class AuthHttpSupport {

    private final AuthCookieProvider cookieProvider; // 쿠키 생성 전략 위임

    public void setAuthResponse(HttpServletResponse response, AuthTokens tokens) {
        // 헤더 주입
        response.setHeader(AuthConstants.AUTHORIZATION_HEADER, AuthConstants.BEARER_PREFIX + tokens.accessToken());

        // 쿠키 주입
        ResponseCookie cookie = cookieProvider.createRefreshTokenCookie(tokens.refreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clearAuthResponse(HttpServletResponse response) {
        ResponseCookie cookie = cookieProvider.createLogoutCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AuthConstants.BEARER_PREFIX)) {
            return bearerToken.substring(AuthConstants.BEARER_PREFIX.length());
        }
        return null;
    }
}
