package com.traveler.post.domain.comment.publisher;

import com.traveler.post.domain.comment.entity.Comment;
import com.traveler.post.domain.comment.enums.CommentEventType;
import com.traveler.post.domain.comment.mapper.CommentMapper;
import com.traveler.post.global.kafka.KafkaTopicProperties;
import com.traveler.post.global.outbox.event.OutboxEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CommentOutboxPublisher extends OutboxEventPublisher {
    private final CommentMapper commentMapper;

    public CommentOutboxPublisher(
            ApplicationEventPublisher eventPublisher,
            CommentMapper commentMapper,
            KafkaTopicProperties topicProperties) {
        super(eventPublisher, topicProperties);
        this.commentMapper = commentMapper;
    }

    public void publishCreated(Comment comment) {
        publish(comment.getId(), CommentEventType.CREATED, commentMapper.toCreatedMsgDTO(comment));
    }

    public void publishUpdated(Comment comment) {
        publish(comment.getId(), CommentEventType.UPDATED, commentMapper.toUpdatedMsgDTO(comment));
    }

    public void publishDeleted(Comment comment) {
        publish(comment.getId(), CommentEventType.DELETED, commentMapper.toDeletedMsgDTO(comment));
    }
}
