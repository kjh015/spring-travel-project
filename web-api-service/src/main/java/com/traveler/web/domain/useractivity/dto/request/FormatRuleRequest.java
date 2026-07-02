package com.traveler.web.domain.useractivity.dto.request;

import com.fasterxml.jackson.databind.JsonNode;

public final class FormatRuleRequest {
    private FormatRuleRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(String name, Boolean isActive, JsonNode defaultValues, JsonNode fieldMappings) {}

    public record UpdateDTO(String name, Boolean isActive, JsonNode defaultValues, JsonNode fieldMappings) {}
}
