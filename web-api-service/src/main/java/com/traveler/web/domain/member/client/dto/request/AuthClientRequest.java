package com.traveler.web.domain.member.client.dto.request;

public final class AuthClientRequest {
    private AuthClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record LoginDTO(String loginId, String password) {}

    public record ReissueDTO(String refreshToken) {}
}
