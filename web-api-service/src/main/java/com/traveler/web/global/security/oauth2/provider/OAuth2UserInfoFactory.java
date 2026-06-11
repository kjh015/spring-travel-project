package com.traveler.web.global.security.oauth2.provider;

import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("kakao")) {
            return KakaoOAuth2UserInfo.from(attributes);
        }
        // 지원하지 않는 소셜 로그인
        throw new WebApiServiceException(WebApiServiceErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
    }
}
