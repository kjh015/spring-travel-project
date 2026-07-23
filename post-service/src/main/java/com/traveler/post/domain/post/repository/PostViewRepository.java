package com.traveler.post.domain.post.repository;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostViewRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String IDEMPOTENCY_PREFIX = "idempotency:trace:";
    private static final String VIEW_COUNT_BUFFER_KEY = "post:view_count:buffer";
    private static final String PROCESSING_KEY = "post:view_count:processing";

    /**
     * 멱등성 키 선점(SET NX PX)과 조회수 증가(HINCRBY)를 하나의 Lua 스크립트로 원자 처리합니다.
     *
     * <p>SET NX 가 성공한 경우(=최초 처리)에만 HINCRBY 를 수행하고 1을 반환합니다.
     * 이미 키가 존재하면(=중복) 아무것도 하지 않고 0을 반환합니다.
     *
     * <p>두 연산이 서버 측에서 한 번에 실행되므로, 기존처럼 "키 선점 성공 후 증가 실패"로
     * 조회수가 영구 누락되는 구간이 존재하지 않습니다. 증가에 성공했을 때만 키가 남기 때문에,
     * Kafka 재시도/재전달이 일어나도 유실도 중복 카운트도 발생하지 않습니다.
     */
    private static final DefaultRedisScript<Long> INCREMENT_IF_FIRST_SCRIPT = new DefaultRedisScript<>(
            """
            if redis.call('SET', KEYS[1], '1', 'NX', 'PX', ARGV[1]) then
                redis.call('HINCRBY', KEYS[2], ARGV[2], 1)
                return 1
            end
            return 0
            """,
            Long.class);

    /**
     * 최초 이벤트일 때만 조회수를 1 증가시키고 true 를 반환합니다. (중복이면 false)
     *
     * @param traceId 멱등성 판별용 traceId
     * @param postId  조회수를 증가시킬 게시글 ID
     * @param ttl     멱등성 키 TTL
     */
    public boolean incrementViewCountIfFirst(String traceId, Long postId, Duration ttl) {
        Long result = redisTemplate.execute(
                INCREMENT_IF_FIRST_SCRIPT,
                List.of(IDEMPOTENCY_PREFIX + traceId, VIEW_COUNT_BUFFER_KEY),
                String.valueOf(ttl.toMillis()),
                String.valueOf(postId));
        return Long.valueOf(1L).equals(result);
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
