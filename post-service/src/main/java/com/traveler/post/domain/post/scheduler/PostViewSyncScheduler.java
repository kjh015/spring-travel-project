package com.traveler.post.domain.post.scheduler;

import com.traveler.post.domain.post.repository.PostViewRepository;
import com.traveler.post.domain.post.service.PostService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostViewSyncScheduler {

    private final PostViewRepository postViewRepository;
    private final PostService postService;

    // 5초(5,000ms)마다 실행
    @Scheduled(fixedDelay = 5000)
    public void flushViewCounts() {
        // 쌓인 데이터가 없으면 즉시 종료
        if (!postViewRepository.hasBufferedViewCounts()) {
            return;
        }

        try {
            // 동시성 보장: 처리용 버퍼로 격리
            postViewRepository.isolateBufferForProcessing();

            // 데이터 추출
            Map<Object, Object> viewCountMap = postViewRepository.getProcessingViewCounts();

            // DB 및 Outbox 동기화 위임
            postService.flushViewCountsToDB(viewCountMap);

            log.info("[ViewCount] {}개의 게시글 조회수가 DB에 동기화되었습니다.", viewCountMap.size());

            // 버퍼 정리
            postViewRepository.deleteProcessingBuffer();
        } catch (Exception e) {
            // 실패 시 PROCESSING_KEY를 보존하여 다음 주기에 재시도
            log.error("[ViewCount] 조회수 동기화 배치 실패. 버퍼를 보존하고 다음 주기에 재시도합니다.", e);
        }
    }
}
