package com.traveler.post.domain.comment.publisher;

import com.traveler.post.domain.comment.dto.message.CommentMessage;
import com.traveler.post.domain.comment.enums.CommentEventType;
import com.traveler.post.global.kafka.KafkaTopicProperties;
import com.traveler.post.global.outbox.event.OutboxEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CommentOutboxPublisher extends OutboxEventPublisher {

    public CommentOutboxPublisher(ApplicationEventPublisher eventPublisher, KafkaTopicProperties topicProperties) {
        super(eventPublisher, topicProperties);
    }

    public void publishCreated(CommentMessage.CreatedDTO payload) {
        publish(payload.commentId(), CommentEventType.CREATED, payload);
    }

    public void publishUpdated(CommentMessage.UpdatedDTO payload) {
        publish(payload.commentId(), CommentEventType.UPDATED, payload);
    }

    public void publishDeleted(CommentMessage.DeletedDTO payload) {
        publish(payload.commentId(), CommentEventType.DELETED, payload);
    }
}
