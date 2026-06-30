package com.traveler.post.domain.post.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.post.domain.post.dto.message.PostMessage;
import com.traveler.post.domain.post.repository.PostViewRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostViewConsumer {

    private final PostViewRepository postViewRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = "${app.kafka.topics.view-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeViewCount(@Payload String payload, Acknowledgment ack) {
        PostMessage.PostViewedDTO dto = objectMapper.readValue(payload, PostMessage.PostViewedDTO.class);

        // 멱등성 검증
        boolean isFirst = postViewRepository.saveIdempotencyKeyIfAbsent(dto.traceId(), Duration.ofMinutes(10));

        if (isFirst) {
            // 조회수 버퍼링
            postViewRepository.incrementViewCount(dto.postId());
        } else {
            log.debug("중복된 조회수 이벤트로 무시되었습니다. traceId: {}", dto.traceId());
        }

        // 수동 커밋
        ack.acknowledge();
    }
}
