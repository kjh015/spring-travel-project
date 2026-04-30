package com.traveler.post.domain.like.listener;

import com.traveler.post.domain.like.dto.event.LikeEvent;
import com.traveler.post.domain.like.publisher.LikeOutboxPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LikeEventListener {
    private final LikeOutboxPublisher likeOutboxPublisher;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleCreatedEvent(LikeEvent.Added event) {
        likeOutboxPublisher.publishAdded(event.likeMsg());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleDeletedEvent(LikeEvent.Removed event) {
        likeOutboxPublisher.publishRemoved(event.likeMsg());
    }
}
