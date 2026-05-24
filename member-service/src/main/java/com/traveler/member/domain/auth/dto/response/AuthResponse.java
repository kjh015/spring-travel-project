package com.traveler.member.domain.auth.dto.response;

import com.traveler.member.domain.auth.dto.AuthTokens;

public final class AuthResponse {
    private AuthResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record LoginDTO(Long memberId, String nickname) {}

    public record LoginResult(AuthTokens tokens, LoginDTO loginInfo) {}
}
