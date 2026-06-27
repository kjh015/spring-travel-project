package com.traveler.useractivity.domain.process.core.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.message.FailInfo;
import com.traveler.useractivity.domain.process.core.message.LogMetadata;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.global.kafka.KafkaProducer;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessDispatcher {

    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;

    public <T> CompletableFuture<Void> dispatchSuccess(String topic, LogMetadata metadata, T data) {
        LogPayload<T> payload = LogPayload.success(metadata, data);
        return send(topic, payload);
    }

    public <T> CompletableFuture<Void> dispatchFailure(String topic, LogMetadata metadata, FailInfo failInfo, T data) {

        LogPayload<T> payload = LogPayload.failure(metadata, failInfo, data);
        return send(topic, payload);
    }

    private CompletableFuture<Void> send(String topic, LogPayload<?> payload) {
        try {
            String jsonString = objectMapper.writeValueAsString(payload);
            // KafkaProducer의 CompletableFuture를 반환받아 Void 타입으로 변환 후 전달
            return kafkaProducer.send(topic, jsonString).thenApply(result -> null);
        } catch (Exception e) {
            log.error(
                    "메시지 직렬화 실패: topic=[{}], traceId=[{}]",
                    topic,
                    payload.metadata().traceId(),
                    e);
            // 동기적 예외(JSON 직렬화 등) 발생 시 실패한 Future 반환
            return CompletableFuture.failedFuture(e);
        }
    }
}
