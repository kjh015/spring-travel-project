package com.traveler.useractivity.domain.history.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;

public final class HistoryResponse {
    private HistoryResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ListDTO(
            @Schema(description = "처리 기록 ID") String historyId,
            @Schema(description = "로그 프로세스 이름", example = "결제 로그 처리") String logProcessName,
            @Schema(description = "처리 성공 여부", example = "true") boolean success,
            @Schema(description = "실패 단계 (실패 시에만 포함)", example = "FILTER") String failStage,
            @Schema(description = "실패한 규칙 이름 (실패 시에만 포함)", example = "에러 로그만 필터링") String failRuleName,
            @Schema(description = "처리 일시") Instant timestamp) {}

    public record DetailDTO(
            @Schema(description = "처리 기록 ID") String historyId,
            @Schema(description = "원본 로그 데이터") Map<String, Object> logData) {}
}
