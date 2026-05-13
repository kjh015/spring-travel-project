package com.traveler.member.global.exception.code;

import com.traveler.common.core.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberServiceErrorCode implements BaseErrorCode {
    // Domain - Member (400)
    MEMBER_BAD_REQUEST(400, "MEMBER400_1", "잘못된 입력 값입니다."),
    MEMBER_ALREADY_DELETED(400, "MEMBER400_2", "이미 삭제된 회원입니다."),
    INVALID_PASSWORD(400, "MEMBER400_3", "비밀번호가 일치하지 않습니다."),
    PASSWORD_SAME_AS_OLD(400, "MEMBER400_4", "새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다."),

    // Domain - Member (404)
    MEMBER_NOT_FOUND(404, "MEMBER404_1", "요청한 회원을 찾을 수 없습니다."),

    // Domain - Member (409)
    MEMBER_EXISTS_LOGINID(409, "MEMBER409_1", "이미 존재하는 아이디입니다."),
    MEMBER_EXISTS_EMAIL(409, "MEMBER409_2", "이미 존재하는 이메일입니다."),
    MEMBER_EXISTS_NICKNAME(409, "MEMBER409_3", "이미 존재하는 닉네임입니다.");

    private final int status;
    private final String code;
    private final String message;
}
