package com.traveler.useractivity.domain.process.deduprule.dto.response;

import com.traveler.useractivity.domain.process.deduprule.vo.DeduplicationSpec;
import java.time.Instant;
import java.util.List;

public final class DeduplicationRuleResponse {
    private DeduplicationRuleResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(Long deduplicationRuleId, Instant createdAt) {}

    public record UpdateDTO(Long deduplicationRuleId, Instant updatedAt) {}

    public record DeleteDTO(Long deduplicationRuleId, Instant deletedAt) {}

    public record ListDTO(
            Long deduplicationRuleId, String name, Instant createdAt, Instant updatedAt, boolean isActive) {}

    public record DetailDTO(
            Long deduplicationRuleId, String name, List<DeduplicationSpec.Rule> rules, boolean isActive) {}
}
