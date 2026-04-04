package com.traveler.post.global.outbox.event;

import com.traveler.post.global.code.PostServiceErrorCode;
import com.traveler.post.global.exception.PostServiceException;
import com.traveler.post.global.kafka.KafkaTopicProperties;
import org.springframework.context.ApplicationEventPublisher;

public abstract class BaseEventPublisher {
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaTopicProperties topicProperties;

    protected BaseEventPublisher(ApplicationEventPublisher eventPublisher, KafkaTopicProperties topicProperties) {
        this.eventPublisher = eventPublisher;
        this.topicProperties = topicProperties;
    }

    protected void publish(Long aggregateId, OutboxEventType type, Object payload) {
        String topic = topicProperties.getTopic(type.getTopicKey());
        if (topic == null || topic.isBlank()) {
            throw new PostServiceException(PostServiceErrorCode.TOPIC_NOT_CONFIGURED);
        }
        eventPublisher.publishEvent(OutboxEvent.of(aggregateId, type, topic, payload));
    }
}
