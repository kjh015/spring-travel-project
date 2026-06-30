package com.traveler.web.domain.useractivity.client.dto.request;

import com.traveler.web.domain.useractivity.vo.DedupSpec;
import java.util.List;

public final class DedupRuleClientRequest {
    private DedupRuleClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(String name, List<DedupSpec.Rule> rules, boolean isActive) {}

    public record UpdateDTO(String name, List<DedupSpec.Rule> rules, boolean isActive) {}
}
