package com.traveler.web.domain.member.dto.request;

import com.traveler.web.domain.member.enums.Gender;
import java.time.LocalDate;

public final class MemberRequest {
    private MemberRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record SignUpDTO(
            String loginId, String password, String email, String nickname, Gender gender, LocalDate birthDate) {}

    public record UpdateDTO(String nickname) {}

    public record UpdatePasswordDTO(String curPassword, String newPassword) {}
}
