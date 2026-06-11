package com.traveler.web.global.security.oauth2;

import com.traveler.web.global.security.oauth2.provider.OAuth2UserInfo;
import com.traveler.web.global.security.oauth2.provider.OAuth2UserInfoFactory;
import com.traveler.web.global.security.ticket.AuthTicketService;
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

    private final AuthTicketService authTicketService;
    private final CookieAuthorizationRequestRepository authorizationRequestRepository;

    @Value("${app.frontend.redirect-uri}") // application.yml에서 프론트엔드 URL 관리 (예: http://localhost:3000/oauth/redirect)
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        // 토큰에서 벤더 ID(kakao, google 등) 추출
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        // OAuth2User 원본 데이터 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Factory를 통해 벤더에 맞는 정규화 객체(OAuth2UserInfo) 생성
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        // AuthFacade를 통해 일회용 인증 티켓(Auth Ticket) 생성 및 Redis 임시 보관
        String authTicket =
                authTicketService.createAuthTicket(userInfo.provider(), userInfo.providerId(), userInfo.email());

        clearAuthenticationAttributes(request, response);

        // 토큰 대신 임시 티켓만 파라미터에 실어 프론트엔드로 리다이렉트
        String targetUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                .queryParam("ticket", authTicket)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequest(request, response);
    }
}
