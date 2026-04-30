package com.traveler.post.global.outbox.service;

import com.traveler.post.global.outbox.entity.Outbox;
import com.traveler.post.global.outbox.repository.OutboxRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxStatusManager {
    private final OutboxRepository outboxRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateToSent(String eventId) {
        outboxRepository
                .findByEventId(eventId)
                .ifPresentOrElse(Outbox::sent, () -> log.warn("Outbox not found for eventId: {}", eventId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateToFailed(String eventId) {
        outboxRepository
                .findByEventId(eventId)
                .ifPresentOrElse(Outbox::failed, () -> log.warn("Outbox not found for eventId: {}", eventId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void prepareRetry(Long outboxId) {
        outboxRepository
                .findById(outboxId)
                .ifPresentOrElse(
                        Outbox::incrementRetryCount, () -> log.warn("Outbox not found for outboxId: {}", outboxId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        outboxRepository.deleteByIdsIn(ids);
    }
}
