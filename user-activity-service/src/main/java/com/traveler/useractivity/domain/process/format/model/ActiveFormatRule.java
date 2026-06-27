package com.traveler.useractivity.domain.process.format.model;

import java.util.Map;

public record ActiveFormatRule(
        Long formatRuleId, Map<String, String> defaultValues, Map<String, String> fieldMappings) {}
