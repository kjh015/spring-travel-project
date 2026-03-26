package com.traveler.post.global.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CompletableFuture<SendResult<String, Object>> send(String topic, Object payload) {
        // 비동기 전송
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, payload);

        // 전송 결과에 따른 후처리 (비동기 콜백)
        return future.whenComplete((result, ex) -> {
            if (ex == null) {
                // 성공 시 로그 기록
                log.info("Kafka 메시지 전송 성공: topic=[{}], offset=[{}]",
                        topic, result.getRecordMetadata().offset());
            } else {
                // 실패 시 에러 로그 기록 및 후속 조치
                log.error("Kafka 메시지 전송 실패: topic=[{}], message=[{}]",
                        topic, ex.getMessage(), ex);
            }
        });
    }
}