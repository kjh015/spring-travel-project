package com.traveler.member.domain.auth.dto.request;

import com.traveler.member.domain.member.enums.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class AuthRequest {
    private AuthRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(description = "일반 로그인 요청 DTO")
    public record LoginDTO(
            @Schema(description = "로그인 ID", example = "traveler123") @NotBlank(message = "로그인 ID는 필수 입력값입니다.")
                    String loginId,
            @Schema(description = "비밀번호", example = "password123!") @NotBlank(message = "비밀번호는 필수 입력값입니다.")
                    String password) {}

    @Schema(description = "토큰 재발급 요청 DTO")
    public record ReissueDTO(
            @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR...")
                    @NotBlank(message = "Refresh Token은 필수 입력값입니다.")
                    String refreshToken) {}

    @Schema(description = "OAuth 로그인 요청 DTO")
    public record OAuthLoginDTO(
            @Schema(description = "OAuth 제공자", example = "KAKAO") @NotNull(message = "OAuth 제공자 정보는 필수입니다.")
                    Provider provider,
            @Schema(description = "OAuth 제공자 식별 ID", example = "1234567890")
                    @NotBlank(message = "Provider ID는 필수 입력값입니다.")
                    String providerId,
            @Schema(description = "사용자 이메일", example = "traveler@example.com")
                    @Email(message = "유효하지 않은 이메일 형식입니다.")
                    @NotBlank(message = "이메일은 필수 입력값입니다.")
                    String email) {}
}
