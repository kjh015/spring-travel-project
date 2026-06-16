package com.traveler.useractivity.global.outbox.event;

public interface OutboxEventType {
    String getAggregateType();

    String getEventType();

    String getTopicKey();
}
