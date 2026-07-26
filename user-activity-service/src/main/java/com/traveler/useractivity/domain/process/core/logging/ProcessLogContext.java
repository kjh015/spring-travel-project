package com.traveler.useractivity.domain.process.core.logging;

import com.traveler.useractivity.domain.process.core.message.LogMetadata;
import java.util.Map;
import java.util.StringJoiner;
import org.slf4j.MDC;

/**
 * 파이프라인 공통 식별자를 MDC로 옮겨 로그 메시지마다 반복 출력되지 않게 하는 헬퍼.
 * 로그 패턴의 %X{trace}, %X{event}, %X{ref} 컬럼과 짝을 이룬다.
 */
public final class ProcessLogContext {

    public static final String TRACE_KEY = "trace";
    public static final String EVENT_KEY = "event";
    public static final String REF_KEY = "ref";

    private static final String EVENT_FIELD = "event";

    // 로그 라인에 함께 노출할 식별 필드. 이벤트 종류가 늘면 여기에 추가한다.
    private static final String[] REF_FIELDS = {"postId", "userId"};

    // 로그 가독성을 위해 UUID 앞부분만 사용 (파이프라인 내 식별에는 충분)
    private static final int TRACE_ID_LENGTH = 8;

    private static final String EMPTY = "-";

    private ProcessLogContext() {}

    /**
     * 포맷팅 전이라 event를 알 수 없는 구간용. event 자리는 프로세스명으로 대체한다.
     */
    public static void put(LogMetadata metadata) {
        MDC.put(TRACE_KEY, shortTraceId(metadata.traceId()));
        MDC.put(EVENT_KEY, defaultIfBlank(metadata.logProcessName()));
        MDC.put(REF_KEY, EMPTY);
    }

    /**
     * 포맷팅된 로그에서 event와 식별 필드를 뽑아 컨텍스트를 채운다.
     * 같은 프로세스(travel-view)라도 pageview/exit를 구분할 수 있게 하는 것이 목적이다.
     */
    public static void put(LogMetadata metadata, Map<String, String> logData) {
        put(metadata);
        if (logData == null || logData.isEmpty()) {
            return;
        }
        String event = logData.get(EVENT_FIELD);
        if (event != null && !event.isBlank()) {
            MDC.put(EVENT_KEY, event);
        }
        MDC.put(REF_KEY, buildRef(logData));
    }

    public static void clear() {
        MDC.remove(TRACE_KEY);
        MDC.remove(EVENT_KEY);
        MDC.remove(REF_KEY);
    }

    private static String buildRef(Map<String, String> logData) {
        StringJoiner joiner = new StringJoiner(" ");
        for (String field : REF_FIELDS) {
            String value = logData.get(field);
            if (value != null && !value.isBlank()) {
                joiner.add(field + "=" + value);
            }
        }
        return joiner.length() == 0 ? EMPTY : joiner.toString();
    }

    private static String shortTraceId(String traceId) {
        if (traceId == null || traceId.length() <= TRACE_ID_LENGTH) {
            return defaultIfBlank(traceId);
        }
        return traceId.substring(0, TRACE_ID_LENGTH);
    }

    private static String defaultIfBlank(String value) {
        return value != null && !value.isBlank() ? value : EMPTY;
    }
}
