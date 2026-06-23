package com.traveler.useractivity.domain.rule.dedup.dto.request;

import com.traveler.useractivity.domain.rule.dedup.vo.DedupSpec;
import jakarta.validation.Valid;
import java.util.List;

public final class DedupRuleRequest {
    private DedupRuleRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(String name, @Valid List<DedupSpec.Rule> rules, boolean isActive) {}

    public record UpdateDTO(String name, @Valid List<DedupSpec.Rule> rules, boolean isActive) {}
}
