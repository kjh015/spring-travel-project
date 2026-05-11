package com.traveler.gateway.auth.support;

import com.traveler.common.core.auth.AuthConstants;
import com.traveler.gateway.exception.ApiGatewayErrorCode;
import com.traveler.gateway.exception.ApiGatewayNoStackException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TokenBlacklistManager {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public Mono<Void> checkBlacklist(String token) {
        return redisTemplate
                .hasKey(AuthConstants.REDIS_BLACKLIST_PREFIX + token)
                .flatMap(isBlacklisted -> Boolean.TRUE.equals(isBlacklisted)
                        ? Mono.error(new ApiGatewayNoStackException(ApiGatewayErrorCode.BLACKLISTED_TOKEN))
                        : Mono.empty());
    }
}
