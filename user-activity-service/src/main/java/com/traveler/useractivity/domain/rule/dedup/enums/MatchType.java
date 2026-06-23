package com.traveler.useractivity.domain.rule.dedup.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;

public enum MatchType {
    EXACT_MATCH("Exact"), // 지정된 값과 정확히 일치해야 함 (예: action == "click")
    ANY_VALUE("Any"); // 값의 내용과 상관없이, 해당 필드가 존재하기만 하면 됨 (기존의 "?")

    private final String value;

    MatchType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MatchType from(String val) {
        for (MatchType type : values()) {
            if (type.value.equalsIgnoreCase(val)) {
                return type;
            }
        }
        throw new UserActivityServiceException(UserActivityServiceErrorCode.DEDUP_UNSUPPORTED_MATCH_TYPE);
    }
}
