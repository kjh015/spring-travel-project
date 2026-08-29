package com.traveler.member.domain.auth.support;

import com.traveler.common.core.auth.AuthConstants;
import com.traveler.member.domain.member.enums.RoleType;
import com.traveler.member.global.exception.MemberServiceException;
import com.traveler.member.global.exception.code.MemberServiceErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Jwk;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PrivateJwk;
import io.jsonwebtoken.security.SignatureException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {
    private final JwtParser jwtParser;
    private final PrivateKey privateKey;
    private final String keyId;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    public JwtTokenProvider(
            @Value("${app.jwt.private-jwk}") String privateJwk,
            @Value("${app.jwt.access-expiration}") long accessTokenExpireTime,
            @Value("${app.jwt.refresh-expiration}") long refreshTokenExpireTime) {
        // 개인 JWK 하나로 서명키와 검증용 공개키를 함께 얻는다
        PrivateJwk<?, ?, ?> jwk = parsePrivateJwk(privateJwk);
        var keyPair = jwk.toKeyPair();
        PrivateKey signingKey = keyPair.getPrivate();
        PublicKey verificationKey = keyPair.getPublic();

        this.privateKey = signingKey;
        this.keyId = jwk.getId();
        this.jwtParser = Jwts.parser().verifyWith(verificationKey).build();
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    private static PrivateJwk<?, ?, ?> parsePrivateJwk(String json) {
        Jwk<?> jwk = Jwks.parser().build().parse(json);
        if (jwk instanceof PrivateJwk<?, ?, ?> privateJwk) {
            return privateJwk;
        }
        throw new IllegalStateException("app.jwt.private-jwk 에 개인키(d)가 없습니다. 공개 JWK를 설정하지 않았는지 확인하세요.");
    }

    public String createAccessToken(Long userId, List<RoleType> roles) {
        return createToken(userId, roles, AuthConstants.TOKEN_TYPE_ACCESS, accessTokenExpireTime);
    }

    public String createRefreshToken(Long userId, List<RoleType> roles) {
        return createToken(userId, roles, AuthConstants.TOKEN_TYPE_REFRESH, refreshTokenExpireTime);
    }

    private String createToken(Long userId, List<RoleType> roles, String tokenType, long validity) {
        Instant now = Instant.now();
        List<String> roleNames = roles.stream().map(RoleType::name).toList();

        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim(AuthConstants.CLAIM_ROLES, roleNames)
                .claim(AuthConstants.CLAIM_TOKEN_TYPE, tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(validity)))
                .signWith(privateKey, Jwts.SIG.ES256);

        if (keyId != null) {
            builder.header().keyId(keyId); // 키 로테이션 대비
        }
        return builder.compact();
    }

    public Claims validateToken(String token) {
        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token: {}", e.getMessage());
            throw new MemberServiceException(MemberServiceErrorCode.EXPIRED_JWT);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new MemberServiceException(determineErrorCode(e));
        }
    }

    public Long getUserId(Claims claims) {
        try {
            return Long.valueOf(claims.getSubject());
        } catch (NumberFormatException | NullPointerException e) {
            throw new MemberServiceException(MemberServiceErrorCode.INVALID_TOKEN_TYPE);
        }
    }

    public List<RoleType> getRoles(Claims claims) {
        Object roles = claims.get(AuthConstants.CLAIM_ROLES);

        if (roles instanceof List<?> list) {
            return list.stream()
                    .filter(String.class::isInstance)
                    .map(obj -> {
                        try {
                            return RoleType.valueOf((String) obj);
                        } catch (IllegalArgumentException e) {
                            log.error("Invalid role name in JWT claim: {}", obj);
                            throw new MemberServiceException(MemberServiceErrorCode.INVALID_TOKEN_TYPE);
                        }
                    })
                    .toList();
        }
        return Collections.emptyList();
    }

    public String getTokenType(Claims claims) {
        return claims.get(AuthConstants.CLAIM_TOKEN_TYPE, String.class);
    }

    public long getRemainingExpirationTime(String token) {
        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            long expirationTime = claims.getExpiration().getTime();
            long currentTime = System.currentTimeMillis();
            return Math.max(0, expirationTime - currentTime);
        } catch (JwtException | IllegalArgumentException e) {
            return 0;
        }
    }

    private MemberServiceErrorCode determineErrorCode(Throwable e) {
        if (e instanceof SignatureException) return MemberServiceErrorCode.SIGNATURE_INVALID_JWT;
        if (e instanceof UnsupportedJwtException) return MemberServiceErrorCode.UNSUPPORTED_JWT;
        if (e instanceof MalformedJwtException) return MemberServiceErrorCode.MALFORMED_JWT;
        return MemberServiceErrorCode.INVALID_TOKEN_TYPE;
    }

    public long getRefreshTokenExpireTime() {
        return this.refreshTokenExpireTime;
    }
}
