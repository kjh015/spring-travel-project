package com.traveler.web.domain.member.client.dto.response;

import com.traveler.web.domain.member.dto.AuthTokens;

public final class AuthClientResponse {
    private AuthClientResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record LoginDTO(Long memberId, String nickname) {}

    public record LoginResult(AuthTokens tokens, LoginDTO loginInfo) {}
}
