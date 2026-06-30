package com.traveler.useractivity.domain.rule.filter.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;

public enum LogicalOperator {
    AND("&&"),
    OR("||");

    private final String value;

    LogicalOperator(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LogicalOperator from(String val) {
        for (LogicalOperator op : values()) {
            if (op.value.equals(val)) {
                return op;
            }
        }
        throw new UserActivityServiceException(UserActivityServiceErrorCode.FILTER_UNSUPPORTED_LOGICAL_OPERATOR);
    }
}
