package com.traveler.web.global.security.oauth2.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.web.domain.member.client.dto.request.AuthClientRequest;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthCodeRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // Lua 스크립트
    private static final String FIND_AND_DELETE_SCRIPT_TEXT = "local value = redis.call('get', KEYS[1]) "
            + "if value then "
            + "    redis.call('del', KEYS[1]) "
            + "end "
            + "return value";
    private static final DefaultRedisScript<Object> FIND_AND_DELETE_SCRIPT =
            new DefaultRedisScript<>(FIND_AND_DELETE_SCRIPT_TEXT, Object.class);

    private static final String CODE_KEY_PREFIX = "auth_code:";
    private static final long CODE_EXPIRATION_SECONDS = 30;

    public void save(String code, AuthClientRequest.OauthLoginDTO oauthLoginDTO) {
        String key = CODE_KEY_PREFIX + code;
        redisTemplate.opsForValue().set(key, oauthLoginDTO, CODE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public Optional<AuthClientRequest.OauthLoginDTO> findAndDelete(String code) {
        String key = CODE_KEY_PREFIX + code;

        // Lua 스크립트 실행
        Object result = redisTemplate.execute(FIND_AND_DELETE_SCRIPT, Collections.singletonList(key));

        if (result == null) {
            return Optional.empty();
        }

        AuthClientRequest.OauthLoginDTO dto = objectMapper.convertValue(result, AuthClientRequest.OauthLoginDTO.class);

        return Optional.of(dto);
    }
}
