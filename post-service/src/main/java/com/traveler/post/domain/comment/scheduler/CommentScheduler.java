package com.traveler.post.domain.comment.scheduler;

import com.traveler.post.domain.comment.repository.CommentRepository;
import com.traveler.post.domain.comment.service.CommentService;
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
public class CommentScheduler {

    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private static final int BATCH_SIZE = 500;

    @Scheduled(cron = "0 30 3 * * *")
    public void cleanupExpiredComments() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        log.info("[CommentCleanup] 만료된 댓글 삭제 배치 시작: 기준일자 {}", threshold);

        int totalDeleted = 0;
        int maxIterations = 1000; // 최대 반복 횟수
        int iteration = 0;
        while (iteration++ < maxIterations) {
            Slice<Long> expiredIds = commentRepository.findExpiredCommentIds(threshold, PageRequest.of(0, BATCH_SIZE));

            if (expiredIds.isEmpty()) break;

            commentService.deleteBatch(expiredIds.getContent());
            totalDeleted += expiredIds.getNumberOfElements();

            log.info("[CommentCleanup] 댓글 배치 삭제 진행 중... 현재까지 삭제된 수: {}", totalDeleted);
        }

        if (iteration >= maxIterations) {
            log.warn("[CommentCleanup] 최대 반복 횟수 도달. 남은 데이터가 있을 수 있습니다.");
        }

        log.info("[CommentCleanup] 만료된 댓글 삭제 배치 완료. 총 삭제 건수: {}", totalDeleted);
    }
}
