package com.traveler.post.global.outbox.event;

public interface OutboxEventType {
    String getAggregateType();

    String getEventType();

    String getTopicKey();
}
