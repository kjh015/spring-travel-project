package com.traveler.useractivity.domain.process.sink.handler;

import com.traveler.useractivity.domain.process.core.message.LogPayload;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface SinkHandler {
    // 해당 핸들러가 처리할 수 있는 조건인지 판별
    boolean supports(Map<String, String> logData);

    // 비동기 처리 수행 후 Future 반환
    CompletableFuture<Void> handle(LogPayload<Map<String, String>> logPayload);
}
