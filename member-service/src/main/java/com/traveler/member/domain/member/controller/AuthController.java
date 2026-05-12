package com.traveler.member.domain.member.controller;

import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.auth.UserContext;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.member.domain.member.dto.request.AuthRequest;
import com.traveler.member.domain.member.dto.response.AuthResponse;
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
    @PostMapping("/login")
    public ApiResponse<AuthResponse.LoginDTO> login(@RequestBody AuthRequest.LoginDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @PostMapping("/logout")
    public ApiResponse<AuthResponse.LogoutDTO> logout(@LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @PostMapping("/tokens/refresh")
    public ApiResponse<AuthResponse.RefreshDTO> reissueRefreshToken() {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }
}
