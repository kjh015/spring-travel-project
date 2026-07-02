package com.traveler.web.domain.useractivity.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.traveler.web.domain.useractivity.enums.ComparisonOperator;
import com.traveler.web.domain.useractivity.enums.LogicalOperator;
import com.traveler.web.domain.useractivity.enums.ValueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class FilterNode {

    private FilterNode() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true)
    @JsonSubTypes({
        @JsonSubTypes.Type(value = Condition.class, name = "condition"),
        @JsonSubTypes.Type(value = Operator.class, name = "operator"),
        @JsonSubTypes.Type(value = Paren.class, name = "left-paren"),
        @JsonSubTypes.Type(value = Paren.class, name = "right-paren")
    })
    public interface Element {
        String type();

        Long groupId();
    }

    public record Condition(
            String type,
            @NotNull Long groupId,
            @NotBlank String field,
            @NotNull ComparisonOperator operator,
            @NotBlank String value,
            @NotNull ValueType valueType)
            implements Element {}

    public record Operator(String type, @NotNull Long groupId, @NotNull LogicalOperator value) implements Element {}

    public record Paren(String type, @NotNull Long groupId) implements Element {}
}
