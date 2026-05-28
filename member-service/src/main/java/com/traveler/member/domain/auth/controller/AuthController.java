package com.traveler.member.domain.auth.controller;

import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.auth.UserContext;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.member.domain.auth.dto.request.AuthRequest;
import com.traveler.member.domain.auth.dto.response.AuthResponse;
import com.traveler.member.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse.LoginResult> login(@RequestBody AuthRequest.LoginDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, authService.login(dto));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@LoginUser UserContext user) {
        authService.logout(user.id(), user.accessToken());
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @PostMapping("/tokens/refresh")
    public ApiResponse<AuthResponse.LoginResult> reissueRefreshToken(@RequestBody AuthRequest.ReissueDTO dto) {
        // BFF가 추출하여 Body로 쏴준 RefreshToken을 기반으로 재발급
        return ApiResponse.onSuccess(SuccessCode.OK, authService.reissue(dto.refreshToken()));
    }
}
