package com.traveler.bff.client;

import com.traveler.bff.dto.service.SignDto;
import com.traveler.bff.dto.service.SignInRequestDto;
import com.traveler.bff.dto.service.SignInResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "sign-api")
public interface SignServiceClient {
    @PostMapping("/sign-api/sign-in")
    SignInResultDto signIn(@RequestBody SignInRequestDto signIn);

    @PostMapping("/sign-api/sign-up")
    String signUp(@RequestBody SignDto signUp);

    @PostMapping("/sign-api/update")
    String updateMember(@RequestBody SignDto data);

    @PostMapping("/sign-api/sign-out")
    String logout();

    @PostMapping("/sign-api/withdraw")
    String withdraw();

    @PostMapping("/sign-api/refresh")
    Map<String, String> refreshToken();

    @PostMapping("/sign-api/test")
    String testAccessToken();

    @PostMapping("/sign-api/nickname")
    String getNickname(@RequestBody Map<String, String> data);

    @GetMapping("/sign-api/nickname-list")
    Map<Long, String> getNicknameList(@RequestBody Set<Long> IDs);
}
