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

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<SendResult<String, String>> send(String topic, String payload) {
        return kafkaTemplate.send(topic, payload).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info(
                        "Kafka 메시지 전송 성공: topic=[{}], offset=[{}]",
                        topic,
                        result.getRecordMetadata().offset());
            } else {
                log.error("Kafka 메시지 전송 실패: topic=[{}]", topic, ex);
            }
        });
    }
}
