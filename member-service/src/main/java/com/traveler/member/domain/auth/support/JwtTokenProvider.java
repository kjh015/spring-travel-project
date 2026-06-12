package com.traveler.member.domain.auth.support;

import com.traveler.common.core.auth.AuthConstants;
import com.traveler.member.domain.member.enums.RoleType;
import com.traveler.member.global.exception.MemberServiceException;
import com.traveler.member.global.exception.code.MemberServiceErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {
    private final JwtParser jwtParser;
    private final SecretKey secretKey;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-expiration}") long accessTokenExpireTime,
            @Value("${app.jwt.refresh-expiration}") long refreshTokenExpireTime) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
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

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim(AuthConstants.CLAIM_ROLES, roleNames)
                .claim(AuthConstants.CLAIM_TOKEN_TYPE, tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(validity)))
                .signWith(secretKey)
                .compact();
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
