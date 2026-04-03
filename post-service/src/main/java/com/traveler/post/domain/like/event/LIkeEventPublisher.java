package com.traveler.post.domain.like.event;

import com.traveler.post.domain.like.entity.Like;
import com.traveler.post.domain.like.enums.LikeEventType;
import com.traveler.post.domain.like.mapper.LikeMapper;
import com.traveler.post.global.kafka.KafkaTopicProperties;
import com.traveler.post.global.outbox.event.BaseEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class LIkeEventPublisher extends BaseEventPublisher {
    private final LikeMapper likeMapper;

    public LIkeEventPublisher(
            ApplicationEventPublisher eventPublisher, LikeMapper likeMapper, KafkaTopicProperties topicProperties) {
        super(eventPublisher, topicProperties);
        this.likeMapper = likeMapper;
    }

    public void publishAdded(Like like) {
        publish(like.getId(), LikeEventType.ADDED, likeMapper.toAddedMessage(like));
    }

    public void publishRemoved(Like like) {
        publish(like.getId(), LikeEventType.REMOVED, likeMapper.toRemovedMessage(like));
    }
}
