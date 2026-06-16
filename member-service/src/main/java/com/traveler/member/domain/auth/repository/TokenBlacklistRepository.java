package com.traveler.member.domain.auth.repository;

import com.traveler.common.core.auth.AuthConstants;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenBlacklistRepository {

    private final StringRedisTemplate redisTemplate;

    /**
     * @param accessToken 블랙리스트 처리할 액세스 토큰
     * @param remainingMilliSeconds 토큰의 남은 유효 시간
     */
    public void save(String accessToken, long remainingMilliSeconds) {
        String key = AuthConstants.REDIS_BLACKLIST_PREFIX + accessToken;
        // 남은 시간만큼만 Redis에 보관 후 자동 삭제
        redisTemplate.opsForValue().set(key, "logout", Duration.ofMillis(remainingMilliSeconds));
    }

    public boolean exists(String accessToken) {
        String key = AuthConstants.REDIS_BLACKLIST_PREFIX + accessToken;
        return redisTemplate.hasKey(key);
    }
}
