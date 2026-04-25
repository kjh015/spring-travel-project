package com.traveler.post.domain.post.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.post.domain.post.dto.message.PostMessage;
import com.traveler.post.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostMessageConsumer {

    private final S3Service s3Service;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = "${spring.kafka.topics.post-commands}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeS3Delete(@Payload String payload, @Header("event-type") String eventType, Acknowledgment ack) {

        log.info("Kafka Consumer [post-commands]: Type=[{}], Payload=[{}]", eventType, payload);
        switch (eventType) {
            case "IMAGE_DELETED", "IMAGE_BATCH_DELETED" -> {
                var dto = objectMapper.readValue(payload, PostMessage.ImagesDeleteDTO.class);
                s3Service.deleteFilesByUrls(dto.imageKeys());
            }
            default -> log.warn("지원하지 않는 커맨드 타입입니다: {}", eventType);
        }
    }
}
