package com.traveler.web.global.security.support;

import com.traveler.common.core.auth.AuthConstants;
import com.traveler.web.domain.member.dto.AuthTokens;
import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthHttpSupport {

    private final AuthCookieProvider cookieProvider; // 쿠키 생성 전략 위임

    public void setAuthResponse(HttpServletResponse response, AuthTokens tokens) {
        if (tokens == null
                || tokens.accessToken() == null
                || tokens.accessToken().isBlank()
                || tokens.refreshToken() == null
                || tokens.refreshToken().isBlank()) {
            throw new WebApiServiceException(WebApiServiceErrorCode.TOKEN_GENERATION_FAILED);
        }
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
}
