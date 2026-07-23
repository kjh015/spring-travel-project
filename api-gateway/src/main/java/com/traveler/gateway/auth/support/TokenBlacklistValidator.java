package com.traveler.gateway.auth.support;

import com.traveler.common.core.auth.AuthConstants;
import com.traveler.gateway.exception.ApiGatewayNoStackException;
import com.traveler.gateway.exception.code.ApiGatewayErrorCode;
import com.traveler.gateway.util.TokenHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TokenBlacklistValidator {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public Mono<Void> checkBlacklist(String token) {
        String hashedToken = TokenHashUtil.hash(token);
        String redisKey = AuthConstants.REDIS_BLACKLIST_PREFIX + hashedToken;
        return redisTemplate
                .hasKey(redisKey)
                .flatMap(isBlacklisted -> Boolean.TRUE.equals(isBlacklisted)
                        ? Mono.error(new ApiGatewayNoStackException(ApiGatewayErrorCode.BLACKLISTED_TOKEN))
                        : Mono.empty());
    }
}
