package com.traveler.web.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public final class AuthResponse {
    private AuthResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(description = "웹 클라이언트 로그인 완료 응답 DTO")
    public record LoginDTO(
            @Schema(description = "로그인한 회원 식별자", example = "1") Long memberId,
            @Schema(description = "로그인한 회원 닉네임", example = "트래블러") String nickname) {}
}
