package com.traveler.search.global.code;

import com.traveler.common.core.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchServiceErrorCode implements BaseErrorCode {
    // Post Domain
    POST_BAD_REQUEST(400, "POST400_1", "잘못된 입력 값입니다."),
    POST_NOT_FOUND(404, "POST404_1", "요청한 게시물을 찾을 수 없습니다."),

    // Comment Domain
    COMMENT_BAD_REQUEST(400, "COMMENT400_1", "잘못된 입력 값입니다."),
    COMMENT_NOT_FOUND(404, "COMMENT404_1", "요청한 댓글을 찾을 수 없습니다."),

    // Infra
    TOPIC_NOT_CONFIGURED(500, "EVENT500_1", "해당 이벤트 타입에 대한 Kafka 토픽 설정이 누락되었습니다.");

    private final int status;
    private final String code;
    private final String message;
}
