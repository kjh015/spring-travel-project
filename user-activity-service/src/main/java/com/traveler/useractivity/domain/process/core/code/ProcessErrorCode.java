package com.traveler.useractivity.domain.process.core.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProcessErrorCode {

    // FORMAT 단계 (포맷팅 실패)
    FORMAT_RULE_NOT_FOUND("FORMAT", "활성화된 포맷 규칙이 없거나 조건에 매칭되지 않습니다."),
    FORMAT_INVALID_DATA("FORMAT", "지원하지 않는 데이터 형식이거나 파싱에 실패했습니다."),

    // FILTER 단계 (조건 탈락)
    FILTER_CONDITION_MISMATCH("FILTER", "로그가 필터 규칙 조건을 통과하지 못했습니다."),
    FILTER_EVALUATION_ERROR("FILTER", "SpEL 표현식 평가 중 오류가 발생했습니다."),

    // DEDUP 단계 (중복 탈락)
    DEDUP_DUPLICATED_LOG("DEDUP", "기존에 적재된 로그와 중복된 로그로 판별되었습니다.");

    private final String stage; // 실패한 파이프라인 단계
    private final String description; // 에러의 상세/기본 사유
}
