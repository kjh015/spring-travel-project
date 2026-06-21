package com.traveler.useractivity.domain.process.filterrule.dto.request;

import com.traveler.useractivity.domain.process.filterrule.vo.FilterRuleNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public final class FilterRuleRequest {
    private FilterRuleRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(
            @NotBlank(message = "필터 규칙 이름은 필수입니다.") String name,
            @NotEmpty(message = "최소 하나 이상의 조건 노드가 필요합니다.") @Valid List<FilterRuleNode.Element> conditions,
            @NotNull boolean isActive) {}

    public record UpdateDTO(
            @NotBlank(message = "필터 규칙 이름은 필수입니다.") String name,
            @NotEmpty(message = "최소 하나 이상의 조건 노드가 필요합니다.") @Valid List<FilterRuleNode.Element> conditions,
            @NotNull boolean isActive) {}
}
