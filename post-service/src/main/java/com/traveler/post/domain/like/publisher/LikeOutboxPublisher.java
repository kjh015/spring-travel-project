package com.traveler.post.domain.like.publisher;

import com.traveler.post.domain.like.dto.message.LikeMessage;
import com.traveler.post.domain.like.enums.LikeEventType;
import com.traveler.post.global.kafka.KafkaTopicProperties;
import com.traveler.post.global.outbox.event.OutboxEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class LikeOutboxPublisher extends OutboxEventPublisher {

    public LikeOutboxPublisher(ApplicationEventPublisher eventPublisher, KafkaTopicProperties topicProperties) {
        super(eventPublisher, topicProperties);
    }

    public void publishAdded(LikeMessage.AddedDTO payload) {
        publish(payload.likeId(), LikeEventType.ADDED, payload);
    }

    public void publishRemoved(LikeMessage.RemovedDTO payload) {
        publish(payload.likeId(), LikeEventType.REMOVED, payload);
    }
}
