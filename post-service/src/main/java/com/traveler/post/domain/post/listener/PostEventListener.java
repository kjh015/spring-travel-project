package com.traveler.post.domain.post.listener;

import com.traveler.post.domain.post.dto.event.PostEvent;
import com.traveler.post.domain.post.publisher.PostOutboxPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostEventListener {
    private final PostOutboxPublisher postOutboxPublisher;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleCreatedEvent(PostEvent.Created event) {
        postOutboxPublisher.publishCreated(event.postMsg());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleUpdatedEvent(PostEvent.Updated event) {
        postOutboxPublisher.publishUpdated(event.postMsg());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleDeletedEvent(PostEvent.Deleted event) {
        postOutboxPublisher.publishDeleted(event.postMsg());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleDeleteImageEvent(PostEvent.ImagesDelete event) {
        postOutboxPublisher.publishImagesDelete(event.postMsg());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleDeleteImageEvent(PostEvent.ImagesDeleteBatch event) {
        postOutboxPublisher.publishImagesDeleteBatch(event.postMsg());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(PostEvent.StatUpdate event) {
        postOutboxPublisher.publishStatUpdated(event.postMsg());
    }
}
