package com.traveler.useractivity.domain.process.deduprule.dto.request;

import com.traveler.useractivity.domain.process.deduprule.vo.DeduplicationSpec;
import jakarta.validation.Valid;
import java.util.List;

public final class DeduplicationRuleRequest {
    private DeduplicationRuleRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(String name, @Valid List<DeduplicationSpec.Rule> rules, boolean isActive) {}

    public record UpdateDTO(String name, @Valid List<DeduplicationSpec.Rule> rules, boolean isActive) {}
}
