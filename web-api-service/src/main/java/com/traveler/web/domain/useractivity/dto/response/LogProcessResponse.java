package com.traveler.web.domain.useractivity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public final class LogProcessResponse {
    private LogProcessResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(
            @Schema(description = "생성된 로그 프로세스 ID", example = "1") Long logProcessId,
            @Schema(description = "생성 일시") Instant createdAt) {}

    public record UpdateDTO(
            @Schema(description = "수정된 로그 프로세스 ID", example = "1") Long logProcessId,
            @Schema(description = "수정 일시") Instant updatedAt) {}

    public record DeleteDTO(
            @Schema(description = "삭제된 로그 프로세스 ID", example = "1") Long logProcessId,
            @Schema(description = "삭제 일시") Instant deletedAt) {}

    public record ListDTO(
            @Schema(description = "로그 프로세스 ID", example = "1") Long logProcessId,
            @Schema(description = "로그 프로세스 이름", example = "결제 로그 처리") String name,
            @Schema(description = "로그 프로세스 설명", example = "결제 완료 로그를 수집하고 정제하는 프로세스") String description,
            @Schema(description = "생성 일시") Instant createdAt,
            @Schema(description = "수정 일시") Instant updatedAt) {}
}
