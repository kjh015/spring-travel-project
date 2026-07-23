package com.traveler.web.domain.useractivity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;

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
        throw new WebApiServiceException(WebApiServiceErrorCode.FILTER_UNSUPPORTED_VALUE_TYPE);
    }
}
