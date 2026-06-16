package com.traveler.useractivity.global.exception.code;

import com.traveler.common.core.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserActivityServiceErrorCode implements BaseErrorCode {
    // Domain - Member

    // Infra - Kafka
    TOPIC_NOT_CONFIGURED(500, "EVENT500_1", "해당 이벤트 타입에 대한 Kafka 토픽 설정이 누락되었습니다.");

    private final int status;
    private final String code;
    private final String message;
}
