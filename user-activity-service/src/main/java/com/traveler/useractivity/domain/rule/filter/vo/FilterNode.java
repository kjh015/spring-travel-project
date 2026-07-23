package com.traveler.useractivity.domain.rule.filter.vo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.traveler.useractivity.domain.rule.filter.enums.ComparisonOperator;
import com.traveler.useractivity.domain.rule.filter.enums.LogicalOperator;
import com.traveler.useractivity.domain.rule.filter.enums.ValueType;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class FilterNode {

    private FilterNode() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME, // 다형성 객체 식별자로 논리적 이름(Name)을 사용
            include = JsonTypeInfo.As.EXISTING_PROPERTY, // JSON 데이터 내부에 포함된 기존 필드를 식별 기준으로 설정
            property = "type", // 구분을 위한 유형 기준 필드로 "type"을 지정
            visible = true // 역직렬화 완료 후에도 하위 DTO 클래스에서 "type" 필드를 조회할 수 있도록 속성을 유지
            )
    @JsonSubTypes({
        // "type" 필드의 값이 "condition"인 경우 Condition 클래스로 매핑하여 역직렬화
        @JsonSubTypes.Type(value = Condition.class, name = "condition"),
        // "type" 필드의 값이 "operator"인 경우 Operator 클래스로 매핑하여 역직렬화
        @JsonSubTypes.Type(value = Operator.class, name = "operator"),
        // "type" 필드의 값이 "left-paren" 또는 "right-paren"인 경우 LeftParen, RightParen 클래스로 매핑하여 역직렬화
        @JsonSubTypes.Type(value = LeftParen.class, name = "left-paren"),
        @JsonSubTypes.Type(value = RightParen.class, name = "right-paren")
    })
    @Schema(
            oneOf = {Condition.class, Operator.class, LeftParen.class, RightParen.class},
            discriminatorProperty = "type",
            discriminatorMapping = {
                @DiscriminatorMapping(value = "condition", schema = Condition.class),
                @DiscriminatorMapping(value = "operator", schema = Operator.class),
                @DiscriminatorMapping(value = "left-paren", schema = LeftParen.class),
                @DiscriminatorMapping(value = "right-paren", schema = RightParen.class)
            })
    public interface Element {
        @Schema(
                description = "노드 타입 (반드시 소문자/케밥케이스)",
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

    public record LeftParen(@Schema(example = "left-paren") String type, @NotNull Long groupId) implements Element {}

    public record RightParen(@Schema(example = "right-paren") String type, @NotNull Long groupId) implements Element {}
}
