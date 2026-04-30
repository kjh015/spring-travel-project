package com.traveler.web.global.code;

import com.traveler.common.core.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WebApiServiceErrorCode implements BaseErrorCode {
    // Domain - Post
    POST_BAD_REQUEST(400, "POST400_1", "잘못된 입력 값입니다."),
    POST_NOT_FOUND(404, "POST404_1", "요청한 게시물을 찾을 수 없습니다."),

    // Domain - Comment
    COMMENT_BAD_REQUEST(400, "COMMENT400_1", "잘못된 입력 값입니다."),
    COMMENT_NOT_FOUND(404, "COMMENT404_1", "요청한 댓글을 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;
}
