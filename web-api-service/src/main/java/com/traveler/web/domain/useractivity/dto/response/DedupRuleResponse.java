package com.traveler.web.domain.useractivity.dto.response;

import com.traveler.web.domain.useractivity.dto.DedupSpec;
import java.time.Instant;
import java.util.List;

public final class DedupRuleResponse {
    private DedupRuleResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(Long dedupRuleId, Instant createdAt) {}

    public record UpdateDTO(Long dedupRuleId, Instant updatedAt) {}

    public record DeleteDTO(Long dedupRuleId, Instant deletedAt) {}

    public record ListDTO(Long dedupRuleId, String name, Instant createdAt, Instant updatedAt, boolean isActive) {}

    public record DetailDTO(Long dedupRuleId, String name, List<DedupSpec.Rule> rules, boolean isActive) {}
}
