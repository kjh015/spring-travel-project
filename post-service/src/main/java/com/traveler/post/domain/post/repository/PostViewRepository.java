package com.traveler.post.domain.post.repository;

import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostViewRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String IDEMPOTENCY_PREFIX = "idempotency:trace:";
    private static final String VIEW_COUNT_BUFFER_KEY = "post:view_count:buffer";
    private static final String PROCESSING_KEY = "post:view_count:processing";

    /**
     * 멱등성 키를 저장합니다. (이미 존재하면 false 반환)
     */
    public boolean saveIdempotencyKeyIfAbsent(String traceId, Duration ttl) {
        String key = IDEMPOTENCY_PREFIX + traceId;
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "1", ttl));
    }

    /**
     * 특정 게시글의 조회수를 1 증가시킵니다. (Redis Hash 사용)
     */
    public void incrementViewCount(Long postId) {
        redisTemplate.opsForHash().increment(VIEW_COUNT_BUFFER_KEY, String.valueOf(postId), 1);
    }

    /**
     * 현재 버퍼링 중인 조회수 데이터가 존재하는지 확인합니다.
     */
    public boolean hasBufferedViewCounts() {
        return redisTemplate.hasKey(VIEW_COUNT_BUFFER_KEY);
    }

    /**
     * 동시성 방어를 위해 현재 버퍼 키를 처리용 키로 이름을 변경합니다.
     */
    public void isolateBufferForProcessing() {
        redisTemplate.rename(VIEW_COUNT_BUFFER_KEY, PROCESSING_KEY);
    }

    /**
     * 처리용 키에 담긴 모든 조회수 데이터를 가져옵니다.
     */
    public Map<Object, Object> getProcessingViewCounts() {
        return redisTemplate.opsForHash().entries(PROCESSING_KEY);
    }

    /**
     * 처리가 완료된 버퍼 데이터를 삭제합니다.
     */
    public void deleteProcessingBuffer() {
        redisTemplate.delete(PROCESSING_KEY);
    }
}
