package com.traveler.web.domain.member.client.dto.response;

import com.traveler.web.domain.member.enums.AvailabilityType;
import com.traveler.web.domain.member.enums.RoleType;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public final class MemberClientResponse {
    private MemberClientResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record SignUpDTO(Long memberId, Instant createdAt) {}

    public record WithdrawDTO(Long memberId, Instant deletedAt) {}

    public record UpdateDTO(Long memberId, Instant updatedAt) {}

    public record UpdatePasswordDTO(Long memberId, Instant updatedAt) {}

    public record MyProfileDTO(
            Long memberId,
            String loginId,
            String email,
            String nickname,
            String gender,
            LocalDate birthDate,
            Integer age,
            List<RoleType> roles) {}

    public record ProfileDTO(Long memberId, String nickname, String gender, LocalDate birthDate, Integer age) {}

    public record AvailabilityDTO(boolean isAvailable, String value, AvailabilityType reason) {}
}
