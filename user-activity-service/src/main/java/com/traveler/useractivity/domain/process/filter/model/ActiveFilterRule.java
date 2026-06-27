package com.traveler.useractivity.domain.process.filter.model;

public record ActiveFilterRule(Long filterRuleId, String name, String expression) {
    public static ActiveFilterRule of(Long filterRuleId, String name, String expression) {
        return new ActiveFilterRule(filterRuleId, name, expression);
    }
}
