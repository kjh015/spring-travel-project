package com.traveler.useractivity.global.outbox.event;

import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import com.traveler.useractivity.global.kafka.KafkaTopicProperties;
import org.springframework.context.ApplicationEventPublisher;

public abstract class OutboxEventPublisher {
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaTopicProperties topicProperties;

    protected OutboxEventPublisher(ApplicationEventPublisher eventPublisher, KafkaTopicProperties topicProperties) {
        this.eventPublisher = eventPublisher;
        this.topicProperties = topicProperties;
    }

    protected void publish(Long aggregateId, OutboxEventType type, Object payload) {
        String topic = topicProperties.getTopic(type.getTopicKey());
        if (topic == null || topic.isBlank()) {
            throw new UserActivityServiceException(UserActivityServiceErrorCode.TOPIC_NOT_CONFIGURED);
        }
        eventPublisher.publishEvent(OutboxEvent.of(aggregateId, type, topic, payload));
    }
}
