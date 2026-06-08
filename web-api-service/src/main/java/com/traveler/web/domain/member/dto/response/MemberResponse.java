package com.traveler.web.domain.member.dto.response;

import com.traveler.web.domain.member.enums.AvailabilityType;
import com.traveler.web.domain.member.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public final class MemberResponse {
    private MemberResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(description = "회원가입 완료 응답 DTO")
    public record SignUpDTO(
            @Schema(description = "회원 식별자", example = "1") Long memberId,
            @Schema(description = "가입 일시") Instant createdAt) {}

    @Schema(description = "회원 탈퇴 응답 DTO")
    public record WithdrawDTO(
            @Schema(description = "탈퇴 처리된 회원 식별자", example = "1") Long memberId,
            @Schema(description = "탈퇴 처리 일시") Instant deletedAt) {}

    @Schema(description = "정보 수정 응답 DTO")
    public record UpdateDTO(
            @Schema(description = "수정된 회원 식별자", example = "1") Long memberId,
            @Schema(description = "수정 일시") Instant updatedAt) {}

    @Schema(description = "비밀번호 변경 응답 DTO")
    public record UpdatePasswordDTO(
            @Schema(description = "회원 식별자", example = "1") Long memberId,
            @Schema(description = "변경 일시") Instant updatedAt) {}

    @Schema(description = "내 프로필 상세 조회 응답 DTO")
    public record MyProfileDTO(
            @Schema(description = "회원 식별자", example = "1") Long memberId,
            @Schema(description = "로그인 ID", example = "traveler123") String loginId,
            @Schema(description = "이메일", example = "traveler@example.com") String email,
            @Schema(description = "닉네임", example = "트래블러") String nickname,
            @Schema(description = "성별", example = "MALE") String gender,
            @Schema(description = "생년월일", example = "1995-05-05") LocalDate birthDate,
            @Schema(description = "나이", example = "28") Integer age,
            @Schema(description = "보유 권한 목록", example = "[\"ROLE_USER\"]") List<RoleType> roles) {}

    @Schema(description = "중복 및 사용 가능 여부 확인 응답 DTO")
    public record AvailabilityDTO(
            @Schema(description = "사용 가능 여부", example = "true") boolean isAvailable,
            @Schema(description = "검사한 입력값", example = "traveler123") String value,
            @Schema(description = "상태 및 사유 코드", example = "AVAILABLE") AvailabilityType reason) {}
}
