package com.traveler.web.global.security.oauth2;

import com.traveler.web.global.security.oauth2.code.AuthCodeService;
import com.traveler.web.global.security.oauth2.provider.OAuth2UserInfo;
import com.traveler.web.global.security.oauth2.provider.OAuth2UserInfoFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthCodeService authCodeService;
    private final CookieAuthorizationRequestRepository authorizationRequestRepository;

    @Value("${app.frontend.redirect-uri}") // application.yml에서 프론트엔드 URL 관리 (예: http://localhost:3000/oauth/redirect)
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        // 토큰에서 벤더 ID(kakao, google 등) 추출 && OAuth2User 원본 데이터 추출
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)
                || !(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {

            log.error(
                    "[OAuth2 SuccessHandler] 잘못된 인증 객체 타입. Authentication: {}, Principal: {}",
                    authentication.getClass().getSimpleName(),
                    authentication.getPrincipal() != null
                            ? authentication.getPrincipal().getClass().getSimpleName()
                            : "null");

            // 실패 시와 동일하게 임시 데이터 파기
            clearAuthenticationAttributes(request, response);

            // 프론트엔드로 시스템 에러 코드 전달
            String errorUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                    .queryParam("error", OAuth2FailureCode.SYSTEM_ERROR.getErrorCode())
                    .build()
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, errorUrl);
            return;
        }
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        // Factory를 통해 벤더에 맞는 정규화 객체(OAuth2UserInfo) 생성
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        // AuthFacade를 통해 일회용 인증 코드(Auth Code) 생성 및 Redis 임시 보관
        String authCode = authCodeService.createAuthCode(userInfo.provider(), userInfo.providerId(), userInfo.email());

        clearAuthenticationAttributes(request, response);

        // 토큰 대신 임시 코드만 파라미터에 실어 프론트엔드로 리다이렉트
        String targetUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                .queryParam("code", authCode)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequest(request, response);
    }
}
