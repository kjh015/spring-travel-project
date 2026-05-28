package com.traveler.web.domain.member.client.dto.request;

import com.traveler.web.domain.member.enums.Gender;
import java.time.LocalDate;

public final class MemberClientRequest {
    private MemberClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record SignUpDTO(
            String loginId, String password, String email, String nickname, Gender gender, LocalDate birthDate) {}

    public record UpdateDTO(String nickname) {}

    public record UpdatePasswordDTO(String curPassword, String newPassword) {}
}
