package com.traveler.member.domain.auth.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenBlacklistRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "blacklist:";

    /**
     * @param accessToken 블랙리스트 처리할 액세스 토큰
     * @param remainingMilliSeconds 토큰의 남은 유효 시간
     */
    public void save(String accessToken, long remainingMilliSeconds) {
        String key = KEY_PREFIX + accessToken;
        // 남은 시간만큼만 Redis에 보관 후 자동 삭제
        redisTemplate.opsForValue().set(key, "logout", Duration.ofMillis(remainingMilliSeconds));
    }

    public boolean exists(String accessToken) {
        String key = KEY_PREFIX + accessToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
