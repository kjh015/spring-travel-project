package com.traveler.post.domain.comment.listener;

import com.traveler.post.domain.comment.dto.event.CommentEvent;
import com.traveler.post.domain.comment.publisher.CommentOutboxPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener {
    private final CommentOutboxPublisher commentOutboxPublisher;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleCreatedEvent(CommentEvent.Created event) {
        commentOutboxPublisher.publishCreated(event.commentMsg());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleUpdatedEvent(CommentEvent.Updated event) {
        commentOutboxPublisher.publishUpdated(event.commentMsg());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleDeletedEvent(CommentEvent.Deleted event) {
        commentOutboxPublisher.publishDeleted(event.commentMsg());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleAdminDeletedEvent(CommentEvent.AdminDeleted event) {
        commentOutboxPublisher.publishDeleted(event.commentMsg());
    }
}
