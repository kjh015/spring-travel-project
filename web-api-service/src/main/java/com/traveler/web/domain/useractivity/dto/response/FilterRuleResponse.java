package com.traveler.web.domain.useractivity.dto.response;

import com.traveler.web.domain.useractivity.vo.FilterNode;
import java.time.Instant;
import java.util.List;

public final class FilterRuleResponse {
    private FilterRuleResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(Long filterRuleId, Instant createdAt) {}

    public record UpdateDTO(Long filterRuleId, Instant updatedAt) {}

    public record DeleteDTO(Long filterRuleId, Instant deletedAt) {}

    public record ListDTO(Long filterRuleId, String name, Instant createdAt, Instant updatedAt, boolean isActive) {}

    public record DetailDTO(
            Long filterRuleId, String name, String expression, List<FilterNode.Element> conditions, boolean isActive) {}
}
