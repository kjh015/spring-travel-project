package com.traveler.post.global.kafka;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<SendResult<String, String>> send(String topic, String eventType, String payload) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, payload);

        // Header 추가 (문자열을 byte[]로 변환하여 삽입)
        record.headers().add("event-type", eventType.getBytes(StandardCharsets.UTF_8));

        return kafkaTemplate.send(record).whenComplete((result, ex) -> {
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
