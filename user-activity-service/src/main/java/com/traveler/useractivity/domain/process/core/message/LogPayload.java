package com.traveler.useractivity.domain.process.core.message;

import com.traveler.useractivity.domain.process.core.code.ProcessErrorCode;
import java.time.Instant;

public record LogPayload<T>(
        String traceId, Long logProcessId, boolean success, ErrorInfo errorInfo, Instant timestamp, T data) {
    public record ErrorInfo(ProcessErrorCode code, Long failRuleId, String detail) {}

    // 성공 시: errorInfo는 null로 세팅
    public static <T> LogPayload<T> success(String traceId, Long logProcessId, T data) {
        return new LogPayload<>(traceId, logProcessId, true, null, Instant.now(), data);
    }

    // 실패 시: 에러 파라미터들을 받아서 ErrorInfo 객체로 조립
    public static <T> LogPayload<T> failure(
            String traceId, Long logProcessId, ProcessErrorCode code, Long failRuleId, String detail, T data) {

        ErrorInfo errorInfo = new ErrorInfo(code, failRuleId, detail);

        return new LogPayload<>(traceId, logProcessId, false, errorInfo, Instant.now(), data);
    }
}
