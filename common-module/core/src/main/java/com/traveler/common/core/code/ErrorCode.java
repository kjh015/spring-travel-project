package com.traveler.common.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode {
    // Common
    BAD_REQUEST(400, "COMMON400_1", "잘못된 입력 값입니다."),
    INVALID_TYPE_VALUE(400, "COMMON400_2", "유효하지 않은 타입 값입니다."),
    NOT_FOUND(404, "COMMON404_1", "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(405, "COMMON405_1", "허용되지 않은 메서드입니다."),
    CONFLICT(409, "COMMON409_1", "이미 존재하는 리소스이거나 데이터 충돌이 발생했습니다."),
    UNSUPPORTED_MEDIA_TYPE(415, "COMMON415_1", "지원하지 않는 미디어 타입(Content-Type)입니다."),
    SWAGGER_ANNOTATION_ERROR(500, "COMMON500_2", "Swagger 오류"),
    INTERNAL_SERVER_ERROR(500, "COMMON500_1", "예기치 않은 서버 에러가 발생했습니다."),

    // JWT
    UNAUTHORIZED(401, "AUTH401_1", "인증이 필요합니다."),
    EXPIRED_JWT(401, "AUTH401_2", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT(401, "AUTH401_3", "지원되지 않는 JWT 토큰입니다."),
    SIGNATURE_INVALID_JWT(401, "AUTH401_4", "유효하지 않은 JWT 시그니처입니다."),
    JWT_NOT_FOUND(401, "AUTH401_5", "JWT 토큰을 찾을 수 없습니다."),
    AUTHENTICATION_FAILED(401, "AUTH401_6", "인증에 실패했습니다."),
    FORBIDDEN(403, "AUTH403_1", "해당 리소스에 접근할 권한이 없습니다."),

    // S3
    S3_INVALID_URL(400, "S3400_1", "유효하지 않은 S3 URL 형식입니다."),
    S3_FILE_NOT_FOUND(404, "S3400_2", "S3에서 해당 파일을 찾을 수 없습니다."),
    S3_ACCESS_DENIED(403, "S3400_3", "S3 버킷에 접근할 권한이 없습니다."),
    S3_UPLOAD_ERROR(500, "S3500_1", "S3 파일 업로드 중 오류가 발생했습니다."),
    S3_DELETE_ERROR(500, "S3500_2", "S3 파일 삭제 중 오류가 발생했습니다."),
    S3_SERVER_ERROR(500, "S3500_3", "AWS S3 서버 자체 오류가 발생했습니다."),

    PAGE_INVALID(400, "PAGE400_1", "유효하지 않은 페이지 범위입니다.");

    private final int status;
    private final String code;
    private final String message;
}
