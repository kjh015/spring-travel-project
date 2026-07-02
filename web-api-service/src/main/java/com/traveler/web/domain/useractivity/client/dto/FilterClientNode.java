package com.traveler.web.domain.useractivity.client.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.traveler.web.domain.useractivity.enums.ComparisonOperator;
import com.traveler.web.domain.useractivity.enums.LogicalOperator;
import com.traveler.web.domain.useractivity.enums.ValueType;

public final class FilterClientNode {

    private FilterClientNode() {
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
    public sealed interface Element permits Condition, Operator, Paren {
        String type();

        Long groupId();
    }

    public record Condition(
            String type, Long groupId, String field, ComparisonOperator operator, String value, ValueType valueType)
            implements Element {}

    public record Operator(String type, Long groupId, LogicalOperator value) implements Element {}

    public record Paren(String type, Long groupId) implements Element {}
}
