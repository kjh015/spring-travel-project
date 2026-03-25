package com.traveler.post.domain.post.scheduler;

import com.traveler.post.domain.post.repository.PostRepository;
import com.traveler.post.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

        while (true) {
            // ID 조회
            Slice<Long> expiredPostIds = postRepository.findExpiredPostIds(threshold, PageRequest.of(0, 500));
            if (expiredPostIds.isEmpty()) break;

            // 배치 단위로 삭제 처리 (개별 트랜잭션)
            postService.deleteBatch(expiredPostIds.getContent());
        }
    }
}