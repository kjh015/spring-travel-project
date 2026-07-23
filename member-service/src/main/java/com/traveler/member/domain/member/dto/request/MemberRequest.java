package com.traveler.member.domain.member.dto.request;

import com.traveler.member.domain.member.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public final class MemberRequest {
    private MemberRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(description = "회원가입 요청 DTO")
    public record SignUpDTO(
            @Schema(description = "로그인 ID (영문/숫자 4~20자)", example = "traveler123")
                    @NotBlank(message = "로그인 ID는 필수입니다.")
                    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "로그인 ID는 영문 및 숫자 조합 4~20자리여야 합니다.")
                    String loginId,
            @Schema(description = "비밀번호 (영문/숫자/특수문자 포함 8~20자)", example = "password123!")
                    @NotBlank(message = "비밀번호는 필수입니다.")
                    @Pattern(
                            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
                            message = "비밀번호는 8~20자리이며, 영문, 숫자, 특수문자를 포함해야 합니다.")
                    String password,
            @Schema(description = "이메일", example = "traveler@example.com")
                    @NotBlank(message = "이메일은 필수입니다.")
                    @Email(message = "유효하지 않은 이메일 형식입니다.")
                    String email,
            @Schema(description = "닉네임 (2~30자)", example = "트래블러")
                    @NotBlank(message = "닉네임은 필수입니다.")
                    @Size(min = 2, max = 30, message = "닉네임은 2자 이상 30자 이하로 입력해주세요.")
                    String nickname,
            @Schema(description = "성별", example = "MALE") @NotNull(message = "성별 선택은 필수입니다.") Gender gender,
            @Schema(description = "생년월일", example = "1995-05-05")
                    @NotNull(message = "생년월일은 필수입니다.")
                    @Past(message = "생년월일은 과거 날짜만 가능합니다.")
                    LocalDate birthDate) {}

    @Schema(description = "회원 정보 수정 요청 DTO")
    public record UpdateDTO(
            @Schema(description = "변경할 닉네임", example = "새로운트래블러")
                    @NotBlank(message = "닉네임은 필수입니다.")
                    @Size(min = 2, max = 30, message = "닉네임은 2자 이상 30자 이하로 입력해주세요.")
                    String nickname,
            @Schema(description = "성별", example = "MALE") Gender gender,
            @Schema(description = "생년월일", example = "1995-05-05") @Past(message = "생년월일은 과거 날짜만 가능합니다.")
                    LocalDate birthDate) {}

    @Schema(description = "비밀번호 변경 요청 DTO")
    public record UpdatePasswordDTO(
            @Schema(description = "현재 비밀번호", example = "oldPassword123!") @NotBlank(message = "현재 비밀번호를 입력해주세요.")
                    String curPassword,
            @Schema(description = "새로운 비밀번호", example = "newPassword123!")
                    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
                    @Pattern(
                            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
                            message = "비밀번호는 8~20자리이며, 영문, 숫자, 특수문자를 포함해야 합니다.")
                    String newPassword) {}
}
