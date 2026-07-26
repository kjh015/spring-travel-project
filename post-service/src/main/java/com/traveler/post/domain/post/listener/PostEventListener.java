package com.traveler.post.domain.post.listener;

import com.traveler.post.domain.post.dto.event.PostEvent;
import com.traveler.post.domain.post.publisher.PostOutboxPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostEventListener {
    private final PostOutboxPublisher postOutboxPublisher;

    @EventListener
    public void handleCreatedEvent(PostEvent.Created event) {
        postOutboxPublisher.publishCreated(event.postMsg());
    }

    @EventListener
    public void handleUpdatedEvent(PostEvent.Updated event) {
        postOutboxPublisher.publishUpdated(event.postMsg());
    }

    @EventListener
    public void handleDeletedEvent(PostEvent.Deleted event) {
        postOutboxPublisher.publishDeleted(event.postMsg());
    }

    @EventListener
    public void handleDeleteImageEvent(PostEvent.ImagesDelete event) {
        postOutboxPublisher.publishImagesDelete(event.postMsg());
    }

    @EventListener
    public void handleDeleteImageEvent(PostEvent.ImagesDeleteBatch event) {
        postOutboxPublisher.publishImagesDeleteBatch(event.postMsg());
    }

    @EventListener
    public void handle(PostEvent.StatUpdate event) {
        postOutboxPublisher.publishStatUpdated(event.postMsg());
    }
}
