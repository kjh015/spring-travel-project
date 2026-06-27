package com.traveler.useractivity.domain.process.format.model;

import com.fasterxml.jackson.databind.JsonNode;

public record ActiveFormatRule(Long formatRuleId, JsonNode defaultValues, JsonNode fieldMappings) {
    public static ActiveFormatRule of(Long formatRuleId, JsonNode defaultValues, JsonNode fieldMappings) {
        return new ActiveFormatRule(formatRuleId, defaultValues, fieldMappings);
    }
}
