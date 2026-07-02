package com.traveler.useractivity.domain.rule.process.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class LogProcessRequest {
    private LogProcessRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(name = "LogProcessCreateRequest")
    public record CreateDTO(
            @NotBlank(message = "로그 프로세스 이름은 필수입니다.") @Schema(description = "로그 프로세스 이름", example = "결제 로그 처리")
                    String name,
            @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
                    @Schema(description = "로그 프로세스 설명", example = "결제 완료 로그를 수집하고 정제하는 프로세스")
                    String description) {}

    @Schema(name = "LogProcessUpdateRequest")
    public record UpdateDTO(
            @NotBlank(message = "로그 프로세스 이름은 필수입니다.") @Schema(description = "로그 프로세스 이름", example = "결제 로그 처리")
                    String name,
            @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
                    @Schema(description = "로그 프로세스 설명", example = "결제 완료 로그를 수집하고 정제하는 프로세스")
                    String description) {}
}
