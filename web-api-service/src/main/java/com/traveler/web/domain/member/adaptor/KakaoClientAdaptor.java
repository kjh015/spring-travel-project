package com.traveler.web.domain.member.adaptor;

import com.traveler.common.core.auth.AuthConstants;
import com.traveler.web.domain.member.client.KakaoApiClient;
import com.traveler.web.domain.member.client.KakaoAuthClient;
import com.traveler.web.domain.member.client.dto.response.KakaoClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoClientAdaptor {
    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;

    @Value("${app.kakao.client-id}")
    private String clientId;

    @Value("${app.kakao.redirect-uri}")
    private String redirectUri;

    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";

    /**
     * 카카오 인가 코드를 받아 토큰 발급 및 유저 정보 조회까지 한 번에 처리합니다.
     */
    public KakaoClientResponse.UserInfoDTO getUserInfo(String code) {
        // 1. 카카오 토큰 발급
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE_AUTHORIZATION_CODE);
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);

        String kakaoToken = kakaoAuthClient.getToken(formData).accessToken();

        // 2. 유저 정보 조회 후 반환
        return kakaoApiClient.getUserInfo(AuthConstants.BEARER_PREFIX + kakaoToken);
    }
}
