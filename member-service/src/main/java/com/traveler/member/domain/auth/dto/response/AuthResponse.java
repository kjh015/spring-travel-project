package com.traveler.member.domain.auth.dto.response;

import com.traveler.member.domain.auth.dto.AuthTokens;
import io.swagger.v3.oas.annotations.media.Schema;

public final class AuthResponse {
    private AuthResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(description = "로그인 유저 정보 DTO")
    public record LoginDTO(
            @Schema(description = "회원 식별자", example = "1") Long memberId,
            @Schema(description = "회원 닉네임", example = "트래블러") String nickname) {}

    @Schema(description = "로그인 결과 (토큰 및 유저 정보)")
    public record LoginResult(
            @Schema(description = "발급된 인증 토큰") AuthTokens tokens,
            @Schema(description = "로그인한 유저 정보") LoginDTO loginInfo) {}
}
