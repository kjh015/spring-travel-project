package com.traveler.gateway.auth.support;

import com.traveler.common.core.auth.AuthConstants;
import com.traveler.gateway.exception.ApiGatewayNoStackException;
import com.traveler.gateway.exception.code.ApiGatewayErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class JwtTokenProvider {

    private final JwtParser jwtParser; // 파서 재사용

    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret) {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public Mono<Claims> validateToken(String token) {
        return Mono.fromCallable(() -> jwtParser.parseSignedClaims(token).getPayload())
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(e -> {
                    ApiGatewayErrorCode errorCode = determineErrorCode(e);
                    // 스택 트레이스 없이 에러 시그널만 전파
                    return Mono.error(new ApiGatewayNoStackException(errorCode));
                });
    }

    public List<String> getRoles(Claims claims) {
        Object roles = claims.get(AuthConstants.CLAIM_ROLES);
        if (roles instanceof List<?> list) {
            return list.stream()
                    .filter(obj -> obj instanceof String)
                    .map(String.class::cast)
                    .toList();
        }
        return Collections.emptyList();
    }

    private ApiGatewayErrorCode determineErrorCode(Throwable e) {
        if (e instanceof SignatureException) return ApiGatewayErrorCode.SIGNATURE_INVALID_JWT;
        if (e instanceof ExpiredJwtException) return ApiGatewayErrorCode.EXPIRED_JWT;
        if (e instanceof UnsupportedJwtException) return ApiGatewayErrorCode.UNSUPPORTED_JWT;
        if (e instanceof MalformedJwtException) return ApiGatewayErrorCode.MALFORMED_JWT;
        return ApiGatewayErrorCode.INVALID_TOKEN_TYPE;
    }
}
