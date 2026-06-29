package com.traveler.web.domain.useractivity.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.Set;

public final class FormatRuleResponse {
    private FormatRuleResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(Long formatRuleId, Instant createdAt) {}

    public record UpdateDTO(Long formatRuleId, Instant updatedAt) {}

    public record DeleteDTO(Long formatRuleId, Instant deletedAt) {}

    public record ListDTO(Long formatRuleId, String name, Instant createdAt, Instant updatedAt, boolean isActive) {}

    public record DetailDTO(
            Long formatRuleId, String name, boolean isActive, JsonNode defaultValues, JsonNode fieldMappings) {}

    public record FieldDTO(Set<String> fields) {}
}
