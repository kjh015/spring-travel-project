package com.traveler.web.domain.useractivity.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.traveler.web.domain.useractivity.enums.ComparisonOperator;
import com.traveler.web.domain.useractivity.enums.LogicalOperator;
import com.traveler.web.domain.useractivity.enums.ValueType;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(
            oneOf = {Condition.class, Operator.class, Paren.class},
            discriminatorProperty = "type",
            discriminatorMapping = {
                @DiscriminatorMapping(value = "condition", schema = Condition.class),
                @DiscriminatorMapping(value = "operator", schema = Operator.class),
                @DiscriminatorMapping(value = "left-paren", schema = Paren.class),
                @DiscriminatorMapping(value = "right-paren", schema = Paren.class)
            })
    public sealed interface Element permits Condition, Operator, Paren {
        @Schema(
                description = "노드 타입 (반드시 소문자/케밥케이스). left-paren/right-paren 모두 Paren 스키마를 사용합니다.",
                allowableValues = {"condition", "operator", "left-paren", "right-paren"})
        String type();

        Long groupId();
    }

    public record Condition(
            @Schema(example = "condition") String type,
            @NotNull Long groupId,
            @NotBlank String field,
            @NotNull ComparisonOperator operator,
            @NotBlank String value,
            @NotNull ValueType valueType)
            implements Element {}

    public record Operator(
            @Schema(example = "operator") String type, @NotNull Long groupId, @NotNull LogicalOperator value)
            implements Element {}

    public record Paren(
            @Schema(
                            example = "left-paren",
                            allowableValues = {"left-paren", "right-paren"})
                    String type,
            @NotNull Long groupId)
            implements Element {}
}
