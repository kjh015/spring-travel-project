package com.traveler.post.global.outbox.event;

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
        eventPublisher.publishEvent(OutboxEvent.of(aggregateId, type, topic, payload));
    }
}
