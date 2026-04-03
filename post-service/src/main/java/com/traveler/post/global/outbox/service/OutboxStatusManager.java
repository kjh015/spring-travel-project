package com.traveler.post.global.outbox.service;

import com.traveler.post.global.outbox.entity.Outbox;
import com.traveler.post.global.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxStatusManager {
    private final OutboxRepository outboxRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateToSent(String eventId) {
        outboxRepository.findByEventId(eventId).ifPresent(Outbox::sent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateToFailed(String eventId) {
        outboxRepository.findByEventId(eventId).ifPresent(Outbox::failed);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void prepareRetry(Long outboxId) {
        outboxRepository.findById(outboxId).ifPresent(Outbox::incrementRetryCount);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        outboxRepository.deleteByIdsIn(ids);
    }
}
