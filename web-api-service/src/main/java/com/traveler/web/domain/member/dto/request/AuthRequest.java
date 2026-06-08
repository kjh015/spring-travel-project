package com.traveler.web.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public final class AuthRequest {
    private AuthRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(description = "웹 클라이언트 로그인 요청 DTO")
    public record LoginDTO(
            @Schema(description = "로그인 ID", example = "traveler123") @NotBlank(message = "로그인 ID를 입력해주세요.")
                    String loginId,
            @Schema(description = "비밀번호", example = "password123!") @NotBlank(message = "비밀번호를 입력해주세요.")
                    String password) {}
}
