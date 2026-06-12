package com.traveler.web.domain.member.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.member.client.dto.request.AuthClientRequest;
import com.traveler.web.domain.member.dto.request.AuthRequest;
import com.traveler.web.domain.member.dto.response.AuthResponse;
import com.traveler.web.domain.member.facade.AuthFacade;
import com.traveler.web.global.security.oauth2.code.AuthCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API (Web)", description = "웹 클라이언트 전용 인증/인가 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthFacade authFacade;
    private final AuthCodeService authCodeService;

    @Operation(summary = "일반 로그인", description = "ID와 비밀번호를 통해 로그인하고 쿠키와 헤더에 토큰을 세팅합니다.")
    @PostMapping("/login")
    public ApiResponse<AuthResponse.LoginDTO> login(
            @Valid @RequestBody AuthRequest.LoginDTO dto, HttpServletResponse response) {
        return ApiResponse.onSuccess(SuccessCode.OK, authFacade.login(dto, response));
    }

    @Operation(summary = "로그아웃", description = "Member 서버의 로그아웃을 호출하고 웹 브라우저의 인증 쿠키를 만료시킵니다.")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        return ApiResponse.onSuccess(SuccessCode.OK, authFacade.logout(response));
    }

    @Operation(summary = "토큰 재발급", description = "쿠키에 저장된 Refresh Token을 사용하여 새로운 토큰 쌍을 재발급합니다.")
    @PostMapping("/tokens/refresh")
    public ApiResponse<AuthResponse.LoginDTO> reissueRefreshToken(
            @Parameter(hidden = true) @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        return ApiResponse.onSuccess(SuccessCode.OK, authFacade.reissue(refreshToken, response));
    }

    @Operation(
            summary = "소셜 로그인 토큰 발급",
            description = "일회용 코드를 제출하여 OAuth2 토큰 쌍을 발급(생성)받습니다."
                    + "AuthCode 발급 API: http://localhost:8000/api/v1/auth/oauth2/authorize/kakao")
    @PostMapping("/oauth2/tokens")
    public ApiResponse<AuthResponse.LoginDTO> createTokensByCode(
            @Valid @RequestBody AuthRequest.AuthCodeDTO dto, HttpServletResponse response) {
        // 코드 검증 및 소모
        AuthClientRequest.OauthLoginDTO oauthLoginDTO = authCodeService.verifyAndConsumeCode(dto.code());
        // 토큰 발급
        AuthResponse.LoginDTO loginResponse = authFacade.oauthLoginByCodeData(oauthLoginDTO, response);

        return ApiResponse.onSuccess(SuccessCode.OK, loginResponse);
    }
}
