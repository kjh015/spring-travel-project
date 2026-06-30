package com.traveler.web.domain.useractivity.client.dto.request;

import com.fasterxml.jackson.databind.JsonNode;

public final class FormatRuleClientRequest {
    private FormatRuleClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(String name, boolean isActive, JsonNode defaultValues, JsonNode fieldMappings) {}

    public record UpdateDTO(String name, boolean isActive, JsonNode defaultValues, JsonNode fieldMappings) {}
}
