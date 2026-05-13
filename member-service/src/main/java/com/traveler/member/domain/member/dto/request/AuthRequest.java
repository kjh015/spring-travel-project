package com.traveler.member.domain.member.dto.request;

public final class AuthRequest {
    private AuthRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record LoginDTO(String loginId, String password) {}
}
