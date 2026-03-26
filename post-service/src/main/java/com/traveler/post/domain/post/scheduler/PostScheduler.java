package com.traveler.post.domain.post.scheduler;

import com.traveler.post.domain.post.repository.PostRepository;
import com.traveler.post.domain.post.service.PostService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostScheduler {

    private final PostRepository postRepository;
    private final PostService postService;
    private static final int BATCH_SIZE = 500;

    // 매일 새벽 3시에 실행 (초 분 시 일 월 요일)
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupExpiredPosts() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        log.info("[PostCleanup] 만료된 게시글 삭제 배치 시작 (기준일시: {})", threshold);

        int totalDeleted = 0;
        while (true) {
            // ID 조회
            Slice<Long> expiredPostIds = postRepository.findExpiredPostIds(threshold, PageRequest.of(0, BATCH_SIZE));

            if (expiredPostIds.isEmpty()) {
                break;
            }

            // 배치 단위로 삭제 처리
            postService.deleteBatch(expiredPostIds.getContent());

            int currentBatchSize = expiredPostIds.getNumberOfElements();
            totalDeleted += currentBatchSize;

            log.info("[PostCleanup] 배치 삭제 진행 중... 현재 배치: {}건, 누적 삭제: {}건", currentBatchSize, totalDeleted);
        }
        log.info("[PostCleanup] 만료된 게시글 삭제 배치 성공 완료. 총 삭제 건수: {}건", totalDeleted);
    }
}
