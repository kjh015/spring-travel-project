package com.traveler.web.global.security.oauth2.provider;

import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;
import java.util.Map;

public record KakaoOAuth2UserInfo(
        String provider, String providerId, String email, String name, Map<String, Object> attributes)
        implements OAuth2UserInfo {

    // Map을 파싱하여 Record를 생성해 내는 정적 팩토리 메서드
    public static KakaoOAuth2UserInfo from(Map<String, Object> attributes) {
        if (attributes.get("id") == null) {
            throw new WebApiServiceException(WebApiServiceErrorCode.INVALID_OAUTH_USER_INFO);
        }
        String providerId = String.valueOf(attributes.get("id"));

        String email = null;
        String name = null;

        if (attributes.get("kakao_account") instanceof Map<?, ?> account) {
            email = (String) account.get("email");
            if (account.get("profile") instanceof Map<?, ?> profile) {
                name = (String) profile.get("nickname");
            }
        }

        return new KakaoOAuth2UserInfo("KAKAO", providerId, email, name, attributes);
    }
}
