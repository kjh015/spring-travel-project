package com.traveler.useractivity.domain.process.filterrule.vo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.traveler.useractivity.domain.process.filterrule.enums.ComparisonOperator;
import com.traveler.useractivity.domain.process.filterrule.enums.LogicalOperator;
import com.traveler.useractivity.domain.process.filterrule.enums.ValueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class FilterRuleNode {

    private FilterRuleNode() {
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
        // "type" 필드의 값이 "left-paren" 또는 "right-paren"인 경우 Paren 클래스로 매핑하여 역직렬화
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
