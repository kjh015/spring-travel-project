package com.traveler.useractivity.global.exception.code;

import com.traveler.common.core.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserActivityServiceErrorCode implements BaseErrorCode {
    // Domain - Format
    FORMAT_RULE_ALREADY_DELETED(400, "FORMAT400_3", "이미 삭제된 포맷규칙입니다."),
    FORMAT_RULE_NOT_FOUND(404, "FORMAT404_1", "요청한 포맷규칙을 찾을 수 없습니다."),

    // Infra - Kafka
    TOPIC_NOT_CONFIGURED(500, "EVENT500_1", "해당 이벤트 타입에 대한 Kafka 토픽 설정이 누락되었습니다."),
    LOG_PROCESS_NOT_FOUND(404, "PROCESS404_1", "요청한 로그 프로세스를 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;
}
