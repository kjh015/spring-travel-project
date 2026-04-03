package com.traveler.post.domain.comment.event;

import com.traveler.post.domain.comment.entity.Comment;
import com.traveler.post.domain.comment.enums.CommentEventType;
import com.traveler.post.domain.comment.mapper.CommentMapper;
import com.traveler.post.global.kafka.KafkaTopicProperties;
import com.traveler.post.global.outbox.event.BaseEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CommentEventPublisher extends BaseEventPublisher {
    private final CommentMapper commentMapper;

    public CommentEventPublisher(
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
