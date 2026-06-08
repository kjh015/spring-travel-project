package com.traveler.web.domain.member.client.dto.request;

public final class AuthClientRequest {
    private AuthClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record LoginDTO(String loginId, String password) {}

    public record ReissueDTO(String refreshToken) {}

    public record OauthLoginDTO(String provider, String providerId, String email) {}
}
