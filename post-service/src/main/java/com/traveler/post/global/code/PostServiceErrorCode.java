package com.traveler.post.global.code;

import com.traveler.common.core.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostServiceErrorCode implements BaseErrorCode {
    // Domain - Post
    POST_BAD_REQUEST(400, "POST400_1", "잘못된 입력 값입니다."),
    POST_NOT_FOUND(404, "POST404_1", "요청한 게시물을 찾을 수 없습니다."),
    POST_IMAGE_DUPLICATE(400, "POST400_2", "중복된 이미지 URL이 포함되어 있습니다."),
    POST_ALREADY_DELETED(400, "POST400_3", "이미 삭제된 게시물입니다."),

    // Domain - Comment
    COMMENT_BAD_REQUEST(400, "COMMENT400_1", "잘못된 입력 값입니다."),
    COMMENT_NOT_FOUND(404, "COMMENT404_1", "요청한 댓글을 찾을 수 없습니다."),
    COMMENT_ALREADY_DELETED(400, "COMMENT400_2", "이미 삭제된 댓글입니다."),

    // Infra - Kafka
    TOPIC_NOT_CONFIGURED(500, "EVENT500_1", "해당 이벤트 타입에 대한 Kafka 토픽 설정이 누락되었습니다.");

    private final int status;
    private final String code;
    private final String message;
}
