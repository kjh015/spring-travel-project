package com.traveler.web.domain.member.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.member.dto.request.AuthRequest;
import com.traveler.web.domain.member.dto.response.AuthResponse;
import com.traveler.web.domain.member.facade.AuthFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthFacade authFacade;

    @PostMapping("/login")
    public ApiResponse<AuthResponse.LoginDTO> login(
            @RequestBody AuthRequest.LoginDTO dto, HttpServletResponse response) {
        return ApiResponse.onSuccess(SuccessCode.OK, authFacade.login(dto, response));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        return ApiResponse.onSuccess(SuccessCode.OK, authFacade.logout(response));
    }

    @PostMapping("/tokens/refresh")
    public ApiResponse<AuthResponse.LoginDTO> reissueRefreshToken(
            @CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) {
        return ApiResponse.onSuccess(SuccessCode.OK, authFacade.reissue(refreshToken, response));
    }
}
