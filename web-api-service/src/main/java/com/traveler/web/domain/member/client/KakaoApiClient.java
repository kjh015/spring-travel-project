package com.traveler.web.domain.member.client;

import com.traveler.web.domain.member.client.dto.response.KakaoClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-api-client", url = "https://kapi.kakao.com")
public interface KakaoApiClient {
    @GetMapping(value = "/v2/user/me")
    KakaoClientResponse.UserInfoDTO getUserInfo(@RequestHeader("Authorization") String accessToken);
}
