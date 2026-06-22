package com.traveler.useractivity.global.exception.code;

import com.traveler.common.core.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserActivityServiceErrorCode implements BaseErrorCode {
    // LogProcess
    LOG_PROCESS_NOT_FOUND(404, "PROCESS404_1", "요청한 로그 프로세스를 찾을 수 없습니다."),

    // FormatRule
    FORMAT_RULE_ALREADY_DELETED(400, "FORMAT400_3", "이미 삭제된 포맷규칙입니다."),
    FORMAT_RULE_NOT_FOUND(404, "FORMAT404_1", "요청한 포맷규칙을 찾을 수 없습니다."),

    // FilterRule
    FILTER_UNSUPPORTED_NODE_TYPE(400, "FILTER400_1", "지원하지 않는 필터 노드 타입입니다."),
    FILTER_UNSUPPORTED_COMPARISON_OPERATOR(400, "FILTER400_2", "지원하지 않는 비교 연산자입니다."),
    FILTER_UNSUPPORTED_LOGICAL_OPERATOR(400, "FILTER400_3", "지원하지 않는 논리 연산자입니다."),
    FILTER_UNSUPPORTED_VALUE_TYPE(400, "FILTER400_4", "지원하지 않는 데이터 타입입니다."),
    FILTER_RULE_ALREADY_DELETED(400, "FILTER400_5", "이미 삭제된 필터규칙입니다."),
    FILTER_RULE_NOT_FOUND(404, "FILTER404_1", "요청한 필터규칙을 찾을 수 없습니다."),

    // DeduplicationRule
    DEDUP_RULE_ALREADY_DELETED(400, "DEDUP400_1", "이미 삭제된 중복제거 규칙입니다."),
    DEDUP_RULE_NOT_FOUND(404, "DEDUP4404_1", "요청한 중복제거 규칙을 찾을 수 없습니다."),

    // Infra - Kafka
    TOPIC_NOT_CONFIGURED(500, "EVENT500_1", "해당 이벤트 타입에 대한 Kafka 토픽 설정이 누락되었습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
