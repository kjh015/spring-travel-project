package com.traveler.web.domain.member.client.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoClientResponse {
    private KakaoClientResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record TokenDTO(@JsonProperty("access_token") String accessToken) {}

    public record UserInfoDTO(Long id, @JsonProperty("kakao_account") KakaoAccount kakaoAccount) {}

    public record KakaoAccount(String email) {}
}
