package com.traveler.useractivity.domain.process.sink.service;

import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.domain.process.sink.handler.SinkHandler;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SinkService {
    private final List<SinkHandler> sinkHandlers;

    public CompletableFuture<Void> sinkLog(LogPayload<Map<String, String>> logPayload) {
        if (logPayload.failInfo() != null) {
            return CompletableFuture.completedFuture(null);
        }

        Map<String, String> logData = logPayload.data();

        // 조건에 맞는 핸들러들의 Future를 모아서 하나의 거대한 비동기 파이프라인으로 결합
        List<CompletableFuture<Void>> futures = sinkHandlers.stream()
                .filter(handler -> handler.supports(logData))
                .map(handler -> handler.handle(logPayload))
                .toList();

        // 주입된 모든 핸들러의 카프카 전송이 완료될 때 최종 완료되는 통합 Future 반환
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
}
