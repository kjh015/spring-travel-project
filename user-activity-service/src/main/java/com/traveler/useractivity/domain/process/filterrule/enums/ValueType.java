package com.traveler.useractivity.domain.process.filterrule.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;

public enum ValueType {
    STRING,
    INT,
    DOUBLE,
    BOOLEAN;

    @JsonCreator
    public static ValueType from(String val) {
        for (ValueType type : values()) {
            if (type.name().equalsIgnoreCase(val)) {
                return type;
            }
        }
        throw new UserActivityServiceException(UserActivityServiceErrorCode.FILTER_UNSUPPORTED_VALUE_TYPE);
    }
}
