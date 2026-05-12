package com.traveler.member.domain.member.dto.response;

public final class MemberResponse {
    private MemberResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record SignUpDTO() {}

    public record WithdrawDTO() {}

    public record UpdateDTO() {}

    public record UpdatePasswordDTO() {}

    public record ListDTO() {}

    public record DetailDTO() {}

    public record NicknameDTO() {}

    public record CheckDTO() {}
}
