package com.traveler.bff.controller;

import com.traveler.bff.client.SignServiceClient;
import com.traveler.bff.dto.service.SignDto;
import com.traveler.bff.dto.service.SignInRequestDto;
import com.traveler.bff.dto.service.SignInResultDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sign")
@RequiredArgsConstructor
public class BffSignController {
    private final SignServiceClient signServiceClient;

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResultDto> signIn(@RequestBody SignInRequestDto signIn) {
        ResponseEntity<SignInResultDto> response = signServiceClient.signIn(signIn);

        // sign-api 응답의 Set-Cookie 헤더 추출
        List<String> setCookieHeaders = response.getHeaders().get(HttpHeaders.SET_COOKIE);

        // Set-Cookie 헤더를 프론트 응답에 복사해서 전달
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(response.getStatusCode());
        if (setCookieHeaders != null) {
            builder.header(HttpHeaders.SET_COOKIE, setCookieHeaders.toArray(new String[0]));
        }

        // 최종 바디(토큰 등) 포함해서 반환
        return builder.body(response.getBody());
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignDto signUp) {
        return signServiceClient.signUp(signUp);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateMember(@RequestBody SignDto data) {
        return signServiceClient.updateMember(data);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        System.out.println("Logout...");
        // 1. 프론트에서 온 쿠키 읽어서 Feign으로 전달
        String cookieHeader = request.getHeader("Cookie");
        ResponseEntity<String> response = signServiceClient.logout(cookieHeader);

        // 2. sign-api 응답의 Set-Cookie 헤더 추출
        List<String> setCookieHeaders = response.getHeaders().get(HttpHeaders.SET_COOKIE);

        // 3. Set-Cookie 헤더를 프론트 응답에 복사해서 전달
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(response.getStatusCode());
        if (setCookieHeaders != null) {
            builder.header(HttpHeaders.SET_COOKIE, setCookieHeaders.toArray(new String[0]));
        }

        // 4. 최종 바디(문구) 포함해서 반환
        return builder.body(response.getBody());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(HttpServletRequest request) {
        // 1. 프론트에서 온 쿠키 읽어서 Feign으로 전달
        String cookieHeader = request.getHeader("Cookie");
        ResponseEntity<String> response = signServiceClient.withdraw(cookieHeader);

        // 2. sign-api 응답의 Set-Cookie 헤더 추출
        List<String> setCookieHeaders = response.getHeaders().get(HttpHeaders.SET_COOKIE);

        // 3. Set-Cookie 헤더를 프론트 응답에 복사해서 전달
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(response.getStatusCode());
        if (setCookieHeaders != null) {
            builder.header(HttpHeaders.SET_COOKIE, setCookieHeaders.toArray(new String[0]));
        }

        // 4. 최종 바디(문구) 포함해서 반환
        return builder.body(response.getBody());
    }

    @PostMapping("/refresh")
    public Map<String, String> refreshToken(HttpServletRequest request) {
        String cookieHeader = request.getHeader("Cookie");
        return signServiceClient.refreshToken(cookieHeader);
    }

    @PostMapping("/test")
    public ResponseEntity<?> testAccessToken() {
        return signServiceClient.testAccessToken();
    }

    @PostMapping("/nickname")
    public String getNickname(@RequestBody Map<String, String> data) {
        return signServiceClient.getNicknameByLoginId(data);
    }
}