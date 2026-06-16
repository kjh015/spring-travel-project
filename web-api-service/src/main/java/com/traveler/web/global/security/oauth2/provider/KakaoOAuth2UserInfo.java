package com.traveler.web.global.security.oauth2.provider;

import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;
import java.util.Map;
import java.util.Optional;

public record KakaoOAuth2UserInfo(
        String provider, String providerId, String email, String name, Map<String, Object> attributes)
        implements OAuth2UserInfo {
    private static final String PROVIDER_NAME = "KAKAO";
    private static final String KEY_ID = "id";
    private static final String KEY_ACCOUNT = "kakao_account";
    private static final String KEY_PROFILE = "profile";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NICKNAME = "nickname";

    // Map을 파싱하여 Record를 생성해 내는 정적 팩토리 메서드
    public static KakaoOAuth2UserInfo from(Map<String, Object> attributes) {
        String providerId = Optional.ofNullable(attributes.get(KEY_ID))
                .map(String::valueOf)
                .orElseThrow(() -> new WebApiServiceException(WebApiServiceErrorCode.INVALID_OAUTH_USER_INFO));

        String email = null;
        String name = null;

        if (attributes.get(KEY_ACCOUNT) instanceof Map<?, ?> account) {
            if (account.get(KEY_EMAIL) instanceof String e) {
                email = e;
            }
            if (account.get(KEY_PROFILE) instanceof Map<?, ?> profile
                    && profile.get(KEY_NICKNAME) instanceof String n) {
                name = n;
            }
        }

        return new KakaoOAuth2UserInfo(PROVIDER_NAME, providerId, email, name, attributes);
    }
}
