package com.traveler.useractivity.domain.process.sink.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.useractivity.domain.process.sink.dto.event.SinkEvent;
import com.traveler.useractivity.domain.process.sink.dto.message.SinkMessage;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.kafka.KafkaProducer;
import com.traveler.useractivity.global.kafka.KafkaTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SinkEventListener {

    private final KafkaProducer kafkaProducer;
    private final KafkaTopicProperties topics;
    private final ObjectMapper objectMapper;

    @EventListener
    public void handlePostViewedEvent(SinkEvent.PostViewed event) {
        SinkMessage.PostViewedDTO message = event.message();

        String topic = topics.viewEvents();
        String payload = serializePayload(message);
        String eventType = SinkEvent.PostViewed.EVENT_TYPE;

        log.debug("조회수 증가 이벤트를 Kafka로 전송합니다. Post ID: {}, EventType: {}", message.postId(), eventType);

        kafkaProducer.send(topic, payload, eventType);
    }

    private String serializePayload(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Kafka Payload 직렬화 실패: {}", e.getMessage());
            throw new UserActivityServiceException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
