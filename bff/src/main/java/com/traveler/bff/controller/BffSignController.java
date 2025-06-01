package com.traveler.bff.controller;

import com.traveler.bff.client.SignServiceClient;
import com.traveler.bff.dto.service.SignDto;
import com.traveler.bff.dto.service.SignInRequestDto;
import com.traveler.bff.dto.service.SignInResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/sign")
@RequiredArgsConstructor
public class BffSignController {
    private final SignServiceClient signServiceClient;

    @PostMapping("/sign-in")
    public SignInResultDto signIn(@RequestBody SignInRequestDto signIn) {
        return signServiceClient.signIn(signIn);
    }

    @PostMapping("/sign-up")
    public String signUp(@RequestBody SignDto signUp) {
        return signServiceClient.signUp(signUp);
    }

    @PostMapping("/update")
    public String updateMember(@RequestBody SignDto data) {
        return signServiceClient.updateMember(data);
    }

    @PostMapping("/sign-out")
    public String logout() {
        return signServiceClient.logout();
    }

    @PostMapping("/withdraw")
    public String withdraw() {
        return signServiceClient.withdraw();
    }

    @PostMapping("/refresh")
    public Map<String, String> refreshToken() {
        return signServiceClient.refreshToken();
    }

    @PostMapping("/test")
    public String testAccessToken() {
        return signServiceClient.testAccessToken();
    }

    @PostMapping("/nickname")
    public String getNickname(@RequestBody Map<String, String> data) {
        return signServiceClient.getNickname(data);
    }
}