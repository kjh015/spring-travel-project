package com.traveler.post.domain.post.enums;

import com.traveler.post.global.outbox.event.OutboxEventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostEventType implements OutboxEventType {
    CREATED("POST", "CREATED", "post-events"),
    UPDATED("POST", "UPDATED", "post-events"),
    DELETED("POST", "DELETED", "post-events"),
    STAT_UPDATED("POST", "STAT_UPDATED", "post-events"),
    IMAGE_DELETE("POST", "IMAGE_DELETED", "post-commands"),
    IMAGE_DELETE_BATCH("POST", "IMAGE_BATCH_DELETED", "post-commands");

    private final String aggregateType;
    private final String eventType;
    private final String topicKey;
}
