package com.traveler.post.global.outbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.post.global.exception.PostServiceException;
import com.traveler.post.global.outbox.entity.Outbox;
import com.traveler.post.global.outbox.event.OutboxEvent;
import com.traveler.post.global.outbox.mapper.OutboxMapper;
import com.traveler.post.global.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final OutboxMapper outboxMapper;
    private final OutboxStatusManager outboxStatusManager;
    private final OutboxRelay outboxRelay;

    private static final int MAX_RETRY_COUNT = 5;
    private static final int BATCH_SIZE_RETRY = 100;
    private static final int BATCH_SIZE_DELETE = 500;

    @Transactional(propagation = Propagation.MANDATORY)
    public void createOutbox(OutboxEvent event) {
        String jsonPayload = serializePayload(event.payload());

        Outbox outbox = outboxMapper.toEntity(event, jsonPayload);

        outboxRepository.save(outbox);
    }

    public void publish(OutboxEvent event) {
        String jsonPayload = serializePayload(event.payload());
        outboxRelay.relayAsync(event.eventId(), event.topic(), jsonPayload);
    }

    public void retry() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);
        List<Outbox> retryCandidates =
                outboxRepository.findRetryableMessages(threshold, MAX_RETRY_COUNT, PageRequest.of(0, BATCH_SIZE_RETRY));

        if (retryCandidates.isEmpty()) return;

        log.info("Outbox 재시도 대상 {}건 발견", retryCandidates.size());
        for (Outbox outbox : retryCandidates) {
            outboxStatusManager.prepareRetry(outbox.getId());
            outboxRelay.relaySync(outbox.getEventId(), outbox.getTopic(), outbox.getPayload());
        }
    }

    public void cleanupOldSentMessages(int daysToKeep) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysToKeep);
        int totalDeleted = 0;

        while (true) {
            Slice<Long> targetIds = outboxRepository.findSentOutboxIds(threshold, PageRequest.of(0, BATCH_SIZE_DELETE));

            if (targetIds.isEmpty()) break;

            outboxStatusManager.deleteBatch(targetIds.getContent());

            totalDeleted += targetIds.getNumberOfElements();
        }
        log.info("[OutboxCleanup] 총 {}건의 완료된 메시지 정리 완료", totalDeleted);
    }

    private String serializePayload(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Outbox payload 직렬화 실패: {}", e.getMessage());
            throw new PostServiceException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
