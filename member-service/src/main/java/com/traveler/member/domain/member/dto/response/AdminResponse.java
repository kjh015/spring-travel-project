package com.traveler.member.domain.member.dto.response;

public final class AdminResponse {
    private AdminResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record GrantAdminDTO() {}

    public record DeleteDTO() {}
}
