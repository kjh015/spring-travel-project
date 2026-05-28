package com.traveler.web.domain.member.dto.response;

import com.traveler.web.domain.member.enums.Gender;
import com.traveler.web.domain.member.enums.RoleType;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public final class AdminResponse {
    private AdminResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record GrantAdminDTO(Long memberId, List<RoleType> roles) {}

    public record DeleteDTO(Long memberId, Instant deletedAt) {}

    public record ListDTO(
            Long memberId,
            String loginId,
            String email,
            String nickname,
            Gender gender,
            LocalDate birthDate,
            List<RoleType> roles,
            Instant createdAt) {}

    public record DetailDTO(
            Long memberId,
            String loginId,
            String email,
            String nickname,
            Gender gender,
            LocalDate birthDate,
            List<RoleType> roles,
            Instant createdAt,
            Instant updatedAt,
            boolean isDeleted,
            Instant deletedAt) {}
}
