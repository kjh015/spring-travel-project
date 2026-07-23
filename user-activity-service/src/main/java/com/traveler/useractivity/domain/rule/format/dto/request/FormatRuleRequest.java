package com.traveler.useractivity.domain.rule.format.dto.request;

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
            @Schema(description = "기본값 매핑 정보. 문자열이 아닌 JSON 객체로 전달해야 합니다.", example = "{\"level\": \"INFO\"}")
                    JsonNode defaultValues,
            @Schema(
                            description = "필드 매핑 정보. 문자열이 아닌 JSON 객체로 전달해야 합니다.",
                            example = "{\"level\": \"level\", \"message\": \"message\"}")
                    JsonNode fieldMappings) {}

    @Schema(name = "FormatRuleUpdateRequest")
    public record UpdateDTO(
            @NotBlank(message = "포맷 규칙 이름은 필수입니다.") @Schema(description = "포맷 규칙 이름", example = "기본 필드 매핑") String name,
            @NotNull(message = "활성화 여부는 필수입니다.") @Schema(description = "활성화 여부", example = "true") Boolean isActive,
            @Schema(description = "기본값 매핑 정보. 문자열이 아닌 JSON 객체로 전달해야 합니다.", example = "{\"level\": \"INFO\"}")
                    JsonNode defaultValues,
            @Schema(
                            description = "필드 매핑 정보. 문자열이 아닌 JSON 객체로 전달해야 합니다.",
                            example = "{\"level\": \"level\", \"message\": \"message\"}")
                    JsonNode fieldMappings) {}
}
