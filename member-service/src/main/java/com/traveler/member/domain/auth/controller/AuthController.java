package com.traveler.member.domain.auth.controller;

import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.auth.UserContext;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.member.domain.auth.dto.request.AuthRequest;
import com.traveler.member.domain.auth.dto.response.AuthResponse;
import com.traveler.member.domain.auth.service.AuthService;
import com.traveler.member.domain.auth.support.AuthHttpSupport;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthHttpSupport authHttpSupport;

    @PostMapping("/login")
    public ApiResponse<AuthResponse.LoginDTO> login(
            @RequestBody AuthRequest.LoginDTO dto, HttpServletResponse response) {
        AuthResponse.LoginResult result = authService.login(dto);

        authHttpSupport.setAuthResponse(response, result.tokens());

        return ApiResponse.onSuccess(SuccessCode.OK, result.loginInfo());
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @LoginUser UserContext user, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = authHttpSupport.resolveAccessToken(request);

        authService.logout(user.id(), accessToken);

        authHttpSupport.clearAuthResponse(response);
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @PostMapping("/tokens/refresh")
    public ApiResponse<AuthResponse.LoginDTO> reissueRefreshToken(
            @CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) {
        AuthResponse.LoginResult result = authService.reissue(refreshToken);

        // 새로운 토큰 세팅 (AT: Header, RT: Cookie)
        authHttpSupport.setAuthResponse(response, result.tokens());

        return ApiResponse.onSuccess(SuccessCode.OK, result.loginInfo());
    }
}
