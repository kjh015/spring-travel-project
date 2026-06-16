package com.traveler.web.global.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final CookieAuthorizationRequestRepository authorizationRequestRepository;

    @Value("${app.frontend.redirect-uri}") // 예: http://localhost:3000/oauth/redirect
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        // 보안 조치: 로그인 실패 시에도 브라우저에 남아있는 임시 상태 쿠키(CSRF 방어용)를 파기합니다.
        clearAuthenticationAttributes(request, response);

        // 에러 코드 분류: 예외 유형에 따라 프론트엔드가 인지할 수 있는 가벼운 에러 키워드를 도출합니다.
        OAuth2FailureCode failureCode = resolveFailureCode(exception);

        log.warn(
                "[OAuth2 Authentication Failure] Code: {}, Description: {}, Original Reason: {}",
                failureCode.getErrorCode(),
                failureCode.getDescription(),
                exception.getMessage());

        // 리다이렉트 URL 생성: 프론트엔드 주소 뒤에 ?error=... 파라미터를 붙입니다.
        String targetUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                .queryParam("error", failureCode.getErrorCode())
                .build()
                .toUriString();

        // 프론트엔드로 무조건 리다이렉트 시켜 제어권을 넘깁니다.
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        authorizationRequestRepository.removeAuthorizationRequest(request, response);
    }

    /**
     * Spring Security 예외를 OAuth2FailureCode로 매핑
     */
    private OAuth2FailureCode resolveFailureCode(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException oauth2Exception) {
            String springErrorCode = oauth2Exception.getError().getErrorCode();
            return OAuth2FailureCode.from(springErrorCode);
        }
        return OAuth2FailureCode.AUTHENTICATION_FAILED;
    }
}
