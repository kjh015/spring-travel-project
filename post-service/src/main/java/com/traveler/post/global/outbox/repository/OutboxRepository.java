package com.traveler.post.global.outbox.repository;

import com.traveler.post.global.outbox.entity.Outbox;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {
    Optional<Outbox> findByEventId(String eventId);

    // 1. INIT 상태로 생성된지 5분이 지났거나, 2. FAILED 상태인 데이터를 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Outbox o WHERE " + "(o.status = 'INIT' AND o.createdAt < :threshold) OR "
            + "(o.status = 'FAILED' AND o.retryCount < :maxRetry)")
    List<Outbox> findRetryableMessages(LocalDateTime threshold, int maxRetry, Pageable pageable);

    @Query("SELECT o.id FROM Outbox o WHERE o.status = 'SENT' AND o.sentAt < :threshold")
    Slice<Long> findSentOutboxIds(LocalDateTime threshold, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Outbox o WHERE o.id IN :ids")
    void deleteByIdsIn(List<Long> ids);
}
