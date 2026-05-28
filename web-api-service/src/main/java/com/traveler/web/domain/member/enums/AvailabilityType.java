package com.traveler.web.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AvailabilityType {
    // 사용 가능 상태
    AVAILABLE("사용 가능한 정보입니다."),

    // 사용 불가능 상태
    DUPLICATED("이미 존재하는 정보입니다."),
    INVALID_FORMAT("형식 정규식 검증에 실패하였습니다."),
    FORBIDDEN_WORD("정책상 허용되지 않는 단어가 포함되어 있습니다."),
    RESERVED_WORD("시스템 예약어로 사용할 수 없습니다.");

    private final String description;
}
