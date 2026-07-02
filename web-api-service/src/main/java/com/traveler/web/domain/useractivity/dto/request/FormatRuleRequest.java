package com.traveler.web.domain.useractivity.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class FormatRuleRequest {
    private FormatRuleRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Schema(name = "FormatRuleCreateRequest")
    public record CreateDTO(
            @NotBlank(message = "포맷 규칙 이름은 필수입니다.") @Schema(description = "포맷 규칙 이름", example = "기본 필드 매핑") String name,
            @NotNull(message = "활성화 여부는 필수입니다.") @Schema(description = "활성화 여부", example = "true") Boolean isActive,
            @Schema(description = "기본값 매핑 정보 (JSON)") JsonNode defaultValues,
            @Schema(description = "필드 매핑 정보 (JSON)") JsonNode fieldMappings) {}

    @Schema(name = "FormatRuleUpdateRequest")
    public record UpdateDTO(
            @NotBlank(message = "포맷 규칙 이름은 필수입니다.") @Schema(description = "포맷 규칙 이름", example = "기본 필드 매핑") String name,
            @NotNull(message = "활성화 여부는 필수입니다.") @Schema(description = "활성화 여부", example = "true") Boolean isActive,
            @Schema(description = "기본값 매핑 정보 (JSON)") JsonNode defaultValues,
            @Schema(description = "필드 매핑 정보 (JSON)") JsonNode fieldMappings) {}
}
