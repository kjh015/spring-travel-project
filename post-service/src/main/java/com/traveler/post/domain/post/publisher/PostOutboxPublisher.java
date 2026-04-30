package com.traveler.post.domain.post.publisher;

import com.traveler.post.domain.post.dto.message.PostMessage;
import com.traveler.post.domain.post.enums.PostEventType;
import com.traveler.post.global.kafka.KafkaTopicProperties;
import com.traveler.post.global.outbox.event.OutboxEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PostOutboxPublisher extends OutboxEventPublisher {

    public PostOutboxPublisher(ApplicationEventPublisher eventPublisher, KafkaTopicProperties topicProperties) {
        super(eventPublisher, topicProperties);
    }

    public void publishCreated(PostMessage.CreatedDTO payload) {
        publish(payload.postId(), PostEventType.CREATED, payload);
    }

    public void publishUpdated(PostMessage.UpdatedDTO payload) {
        publish(payload.postId(), PostEventType.UPDATED, payload);
    }

    public void publishDeleted(PostMessage.DeletedDTO payload) {
        publish(payload.postId(), PostEventType.DELETED, payload);
    }

    public void publishImagesDelete(PostMessage.ImagesDeleteDTO payload) {
        if (payload.imageKeys() == null || payload.imageKeys().isEmpty()) return;

        publish(payload.postId(), PostEventType.IMAGE_DELETE, payload);
    }

    public void publishImagesDeleteBatch(PostMessage.ImagesDeleteDTO payload) {
        if (payload.imageKeys() == null || payload.imageKeys().isEmpty()) return;

        // 대표 ID로 첫 번째 ID 사용
        publish(payload.postId(), PostEventType.IMAGE_DELETE_BATCH, payload);
    }

    public void publishStatUpdated(PostMessage.UpdateStatDTO payload) {
        if (payload == null) return;

        publish(payload.postId(), PostEventType.STAT_UPDATED, payload);
    }
}
