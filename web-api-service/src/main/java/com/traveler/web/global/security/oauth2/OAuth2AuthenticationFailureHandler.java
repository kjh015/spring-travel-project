package com.traveler.web.global.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
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
        log.warn("[OAuth2 Authentication Failure] Reason: {}", exception.getMessage());

        // 보안 조치: 로그인 실패 시에도 브라우저에 남아있는 임시 상태 쿠키(CSRF 방어용)를 파기합니다.
        clearAuthenticationAttributes(request, response);

        // 에러 코드 분류: 예외 유형에 따라 프론트엔드가 인지할 수 있는 가벼운 에러 키워드를 도출합니다.
        String errorCode = resolveErrorCode(exception);

        // 리다이렉트 URL 생성: 프론트엔드 주소 뒤에 ?error=... 파라미터를 붙입니다.
        String targetUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                .queryParam("error", errorCode)
                .build()
                .toUriString();

        // 프론트엔드로 무조건 리다이렉트 시켜 제어권을 넘깁니다.
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        authorizationRequestRepository.removeAuthorizationRequest(request, response);
    }

    /**
     * Spring Security 예외 메시지를 프론트엔드용 가벼운 에러 코드로 매핑
     */
    private String resolveErrorCode(AuthenticationException exception) {
        String message = exception.getMessage();
        if (message == null) return "unknown_error";

        // 사용자가 카카오 창에서 동의 거부/취소를 누른 경우
        if (message.contains("access_denied")) {
            return "access_denied";
        }
        // 인가 코드 교환 중 카카오 서버와 통신 타임아웃 등이 발생한 경우
        if (message.contains("invalid_token_response") || message.contains("connection")) {
            return "provider_server_error";
        }
        // 의도적인 공격이나 세션 만료로 state(CSRF) 값이 맞지 않는 경우
        if (message.contains("authorization_request_not_found")) {
            return "session_expired";
        }

        return "authentication_failed";
    }
}
