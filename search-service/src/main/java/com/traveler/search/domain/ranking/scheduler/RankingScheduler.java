package com.traveler.search.domain.ranking.scheduler;

import com.traveler.search.domain.ranking.dto.response.RankingResponse;
import com.traveler.search.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class RankingScheduler {
    private final RankingService rankingService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String RANKING_CHANNEL = "ranking:live";
    private static final String RANKING_CACHE_KEY = "ranking:latest";

    // 이전 집계 결과를 보관 (메모리 캐시)
    private RankingResponse.LiveDTO lastResponse;

    /**
     * 10초마다 인기 순위를 집계하고, 변화가 있을 경우에만 Redis Pub/Sub으로 발행합니다.
     */
    @Scheduled(fixedRate = 10000)
    public void refreshAndPublishRankings() {
        // 최신 데이터 집계
        RankingResponse.LiveDTO currentResponse = rankingService.fetchLiveRanking();

        // 이전 데이터와 비교
        if (currentResponse.equals(lastResponse)) {
            return;
        }

        // 변화가 있을 때만 Redis에 기록 및 발행
        this.lastResponse = currentResponse;
        redisTemplate.opsForValue().set(RANKING_CACHE_KEY, currentResponse);
        redisTemplate.convertAndSend(RANKING_CHANNEL, currentResponse);
    }
}
