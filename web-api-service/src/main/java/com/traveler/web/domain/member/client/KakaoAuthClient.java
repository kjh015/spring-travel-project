package com.traveler.web.domain.member.client;

import com.traveler.web.domain.member.client.dto.response.KakaoClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kakao-auth-client", url = "https://kauth.kakao.com")
public interface KakaoAuthClient {
    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoClientResponse.TokenDTO getToken(@RequestBody MultiValueMap<String, String> formData);
}
