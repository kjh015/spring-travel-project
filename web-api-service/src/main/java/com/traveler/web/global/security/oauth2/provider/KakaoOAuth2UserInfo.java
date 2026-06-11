package com.traveler.web.global.security.oauth2.provider;

import java.util.Map;
import java.util.Optional;

public record KakaoOAuth2UserInfo(
        String provider, String providerId, String email, String name, Map<String, Object> attributes)
        implements OAuth2UserInfo {

    // Map을 파싱하여 Record를 생성해 내는 정적 팩토리 메서드
    @SuppressWarnings("unchecked")
    public static KakaoOAuth2UserInfo from(Map<String, Object> attributes) {
        String providerId = String.valueOf(attributes.get("id"));

        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        String email = Optional.ofNullable(account)
                .map(acc -> (String) acc.get("email"))
                .orElse(null);

        String name = Optional.ofNullable(account)
                .map(acc -> (Map<String, Object>) acc.get("profile"))
                .map(profile -> (String) profile.get("nickname"))
                .orElse(null);

        return new KakaoOAuth2UserInfo("KAKAO", providerId, email, name, attributes);
    }
}
