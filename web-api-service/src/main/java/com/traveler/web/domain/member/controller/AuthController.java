package com.traveler.web.domain.member.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.member.dto.request.AuthRequest;
import com.traveler.web.domain.member.dto.response.AuthResponse;
import com.traveler.web.domain.member.facade.AuthFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

        // 쿠키가 존재하지 않을 경우에 대한 예외 처리는 Facade 혹은 GlobalExceptionHandler에서 담당해야 합니다.
        return ApiResponse.onSuccess(SuccessCode.OK, authFacade.reissue(refreshToken, response));
    }

    @Operation(summary = "카카오 소셜 로그인", description = "카카오 인가 코드를 전달받아 소셜 로그인을 수행하고 인증 쿠키를 세팅합니다.")
    @GetMapping("/kakao/login")
    public ApiResponse<AuthResponse.LoginDTO> kakaoLogin(
            @Parameter(description = "카카오에서 발급받은 인가 코드", example = "kakao_auth_code_example")
                    @NotBlank(message = "인가 코드는 필수입니다.")
                    @RequestParam("code")
                    String code,
            HttpServletResponse response) {
        return ApiResponse.onSuccess(SuccessCode.OK, authFacade.kakaoLogin(code, response));
    }
}
