package com.traveler.member.domain.auth.repository;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "refreshToken:";

    /**
     * @param memberId 회원 식별자 (Key)
     * @param refreshToken 리프레시 토큰 값 (Value)
     * @param ttl 만료 시간 (JWT의 리프레시 토큰 만료 시간과 동기화)
     */
    public void save(Long memberId, String refreshToken, long ttl) {
        String key = KEY_PREFIX + memberId;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(ttl));
    }

    public Optional<String> findByMemberId(Long memberId) {
        String key = KEY_PREFIX + memberId;
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void deleteByMemberId(Long memberId) {
        String key = KEY_PREFIX + memberId;
        redisTemplate.delete(key);
    }
}
