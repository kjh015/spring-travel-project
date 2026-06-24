package com.traveler.useractivity.domain.process.dedup.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DedupHistoryRepository {

    private final StringRedisTemplate redisTemplate;

    // 💡 규칙 ID와 중복 키를 조합하여 완벽한 고유 키 생성
    private static final String KEY_PREFIX = "dedup:rule:";

    /**
     * Redis에 중복 제거 키를 저장합니다. (SETNX 활용)
     * * @param ruleId 중복 제거 규칙 ID
     * @param dedupKey 추출된 고유 중복 키 (예: action:click|user_id:123)
     * @param ttlSeconds 만료 시간 (초)
     * @return true (저장 성공 = 최초 진입), false (이미 존재 = 중복)
     */
    public boolean saveIfAbsent(Long ruleId, String dedupKey, long ttlSeconds) {
        String key = KEY_PREFIX + ruleId + ":" + dedupKey;

        // 값("1")은 중요하지 않으며, 키의 '존재 여부'와 'TTL'이 핵심입니다.
        Boolean isSaved = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofSeconds(ttlSeconds));

        return Boolean.TRUE.equals(isSaved);
    }
}
