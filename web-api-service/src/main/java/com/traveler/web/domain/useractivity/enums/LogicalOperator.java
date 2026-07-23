package com.traveler.web.domain.useractivity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;

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
        throw new WebApiServiceException(WebApiServiceErrorCode.FILTER_UNSUPPORTED_LOGICAL_OPERATOR);
    }
}
