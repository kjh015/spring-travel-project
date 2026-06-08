package com.traveler.web.domain.member.facade;

import com.traveler.web.domain.member.adaptor.AuthClientAdaptor;
import com.traveler.web.domain.member.adaptor.KakaoClientAdaptor;
import com.traveler.web.domain.member.client.dto.request.AuthClientRequest;
import com.traveler.web.domain.member.client.dto.response.AuthClientResponse;
import com.traveler.web.domain.member.client.dto.response.KakaoClientResponse;
import com.traveler.web.domain.member.dto.request.AuthRequest;
import com.traveler.web.domain.member.dto.response.AuthResponse;
import com.traveler.web.domain.member.mapper.AuthMapper;
import com.traveler.web.domain.member.support.AuthHttpSupport;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {
    private final AuthClientAdaptor authClientAdaptor;
    private final KakaoClientAdaptor kakaoClientAdaptor;
    private final AuthMapper authMapper;
    private final AuthHttpSupport authHttpSupport;

    public AuthResponse.LoginDTO login(AuthRequest.LoginDTO dto, HttpServletResponse response) {
        AuthClientRequest.LoginDTO clientRequest = authMapper.toClientLoginRequest(dto);

        AuthClientResponse.LoginResult clientResponse = authClientAdaptor.login(clientRequest);

        // BFF에서 브라우저 응답 헤더 및 쿠키 굽기
        authHttpSupport.setAuthResponse(response, clientResponse.tokens());

        return authMapper.toResponseLoginDTO(clientResponse.loginInfo());
    }

    public Void logout(HttpServletResponse response) {
        authClientAdaptor.logout();

        authHttpSupport.clearAuthResponse(response);
        return null;
    }

    public AuthResponse.LoginDTO reissue(String refreshToken, HttpServletResponse response) {
        AuthClientRequest.ReissueDTO clientRequest = authMapper.toClientReissueDTO(refreshToken);

        AuthClientResponse.LoginResult clientResponse = authClientAdaptor.reissue(clientRequest);

        authHttpSupport.setAuthResponse(response, clientResponse.tokens());

        return authMapper.toResponseLoginDTO(clientResponse.loginInfo());
    }

    public AuthResponse.LoginDTO kakaoLogin(String code, HttpServletResponse response) {
        // 1. 카카오 통신 (토큰 발급 + 유저 정보 조회 캡슐화)
        KakaoClientResponse.UserInfoDTO kakaoUserInfo = kakaoClientAdaptor.getUserInfo(code);

        // 2. OCP 정규화 DTO 변환
        AuthClientRequest.OauthLoginDTO oauthLoginReq = authMapper.toOauthLoginDTO(kakaoUserInfo);

        // 3. Member-Service로 전달하여 우리 서비스 토큰 발급
        AuthClientResponse.LoginResult loginResult = authClientAdaptor.oauthLogin(oauthLoginReq);

        // 4. 브라우저 세션 굽기
        authHttpSupport.setAuthResponse(response, loginResult.tokens());

        return authMapper.toResponseLoginDTO(loginResult.loginInfo());
    }
}
