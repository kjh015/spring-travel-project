package com.traveler.web.global.security.ticket;

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
public class AuthTicketRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    // Lua 스크립트
    private static final String FIND_AND_DELETE_SCRIPT_TEXT =
            "local value = redis.call('get', KEYS[1]) " + "if value then "
                    + "    redis.call('del', KEYS[1]) "
                    + "end "
                    + "return value";
    private final DefaultRedisScript<AuthClientRequest.OauthLoginDTO> findAndDeleteScript =
            new DefaultRedisScript<>(FIND_AND_DELETE_SCRIPT_TEXT, AuthClientRequest.OauthLoginDTO.class);

    private static final String TICKET_KEY_PREFIX = "auth_ticket:";
    private static final long TICKET_EXPIRATION_SECONDS = 30;

    public void save(String ticket, AuthClientRequest.OauthLoginDTO oauthLoginDTO) {
        String key = TICKET_KEY_PREFIX + ticket;
        redisTemplate.opsForValue().set(key, oauthLoginDTO, TICKET_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public Optional<AuthClientRequest.OauthLoginDTO> findAndDelete(String ticket) {
        String key = TICKET_KEY_PREFIX + ticket;

        // 3. Lua 스크립트 실행: get과 del이 원자적(Atomic)으로 수행됨
        AuthClientRequest.OauthLoginDTO result = redisTemplate.execute(
                findAndDeleteScript, Collections.singletonList(key) // KEYS[1]에 매핑될 키 목록
                );

        return Optional.of(result);
    }
}
