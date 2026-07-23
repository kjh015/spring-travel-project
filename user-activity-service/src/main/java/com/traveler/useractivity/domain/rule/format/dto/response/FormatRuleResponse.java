package com.traveler.useractivity.domain.rule.format.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Set;

public final class FormatRuleResponse {
    private FormatRuleResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(
            @Schema(description = "생성된 포맷 규칙 ID", example = "1") Long formatRuleId,
            @Schema(description = "생성 일시") Instant createdAt) {}

    public record UpdateDTO(
            @Schema(description = "수정된 포맷 규칙 ID", example = "1") Long formatRuleId,
            @Schema(description = "수정 일시") Instant updatedAt) {}

    public record DeleteDTO(
            @Schema(description = "삭제된 포맷 규칙 ID", example = "1") Long formatRuleId,
            @Schema(description = "삭제 일시") Instant deletedAt) {}

    public record ListDTO(
            @Schema(description = "포맷 규칙 ID", example = "1") Long formatRuleId,
            @Schema(description = "포맷 규칙 이름", example = "기본 필드 매핑") String name,
            @Schema(description = "생성 일시") Instant createdAt,
            @Schema(description = "수정 일시") Instant updatedAt,
            @Schema(description = "활성화 여부", example = "true") boolean isActive) {}

    public record DetailDTO(
            @Schema(description = "포맷 규칙 ID", example = "1") Long formatRuleId,
            @Schema(description = "포맷 규칙 이름", example = "기본 필드 매핑") String name,
            @Schema(description = "활성화 여부", example = "true") boolean isActive,
            @Schema(description = "기본값 매핑 정보 (JSON)") JsonNode defaultValues,
            @Schema(description = "필드 매핑 정보 (JSON)") JsonNode fieldMappings) {}

    public record FieldDTO(@Schema(description = "활성 포맷 규칙의 필드명 집합") Set<String> fields) {}
}
