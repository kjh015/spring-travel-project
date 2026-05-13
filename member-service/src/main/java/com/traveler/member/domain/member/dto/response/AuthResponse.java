package com.traveler.member.domain.member.dto.response;

public final class AuthResponse {
    private AuthResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record LoginDTO(Long memberId, String nickname) {}

    public record LogoutDTO() {}

    public record RefreshDTO() {}
}
