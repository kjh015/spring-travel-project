package com.traveler.useractivity.domain.process.deduprule.dto.request;

public final class DeduplicationRuleRequest {
    private DeduplicationRuleRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO() {}
}
