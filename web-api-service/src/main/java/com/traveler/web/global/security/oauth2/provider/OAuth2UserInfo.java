package com.traveler.web.global.security.oauth2.provider;

import java.util.Map;

public interface OAuth2UserInfo {
    String provider(); // KAKAO, GOOGLE 등

    String providerId(); // 벤더가 발급한 고유 식별자

    String email(); // 사용자 이메일

    String name(); // 사용자 이름/닉네임

    Map<String, Object> attributes(); // 원본 데이터
}
