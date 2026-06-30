package com.traveler.web.domain.useractivity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;

public enum ComparisonOperator {
    EQUALS("Equals"),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<="),
    EQUAL("=="),
    NOT_EQUAL("!=");

    private final String value;

    ComparisonOperator(String value) {
        this.value = value;
    }

    @JsonValue // 백엔드 -> 프론트엔드 응답 시 이 값을 사용
    public String getValue() {
        return value;
    }

    @JsonCreator // 프론트엔드 -> 백엔드 요청 시 이 값을 기준으로 Enum 생성
    public static ComparisonOperator from(String val) {
        for (ComparisonOperator op : values()) {
            if (op.value.equalsIgnoreCase(val)) {
                return op;
            }
        }
        throw new WebApiServiceException(WebApiServiceErrorCode.FILTER_UNSUPPORTED_COMPARISON_OPERATOR);
    }
}
