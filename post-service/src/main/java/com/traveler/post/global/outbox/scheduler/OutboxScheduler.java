package com.traveler.post.global.outbox.scheduler;

import com.traveler.post.global.outbox.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {
    private final OutboxService outboxService;

    @Scheduled(fixedDelay = 180000) // 3분마다 실행
    public void retryFailedMessages() {
        outboxService.retry();
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void cleanupOldSentMessages() {
        outboxService.cleanupOldSentMessages(7);
    }
}
