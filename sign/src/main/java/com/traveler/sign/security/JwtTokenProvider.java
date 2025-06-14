package com.traveler.sign.security;


import com.traveler.sign.service.MemberDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final MemberDetailsService memberDetailsService;

    public JwtTokenProvider(MemberDetailsService memberDetailsService) {
        this.memberDetailsService = memberDetailsService;
    }
    private Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${springboot.jwt.secret}")
    private String secretKey = "randomKey";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 12;	   // 12시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 3;  // 3일


    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // AccessToken 생성
    public String createAccessToken(String userId, List<String> roles) {
        return createToken(userId, roles, ACCESS_TOKEN_EXPIRE_TIME);
    }

    // RefreshToken 생성
    public String createRefreshToken(String userId, List<String> roles) {
        return createToken(userId, roles, REFRESH_TOKEN_EXPIRE_TIME);
    }


    public String createToken(String loginId, List<String> roles, long validity){
        logger.info("[createToken] 토큰 생성 시작");
        Date now = new Date();
        String token = Jwts.builder()
                .subject(loginId)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + validity))
                .signWith(getSigningKey())
                .compact();
        logger.info("[createToken] 토큰 생성 완료");
        return token;
    }

    public String getUsername(String token){
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;
    }
    public List<String> getRoles(String token){
        Object roles = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().get("roles");
        List<String> info;
        if (roles instanceof List<?>) {
            info = ((List<?>) roles).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        } else {
            info = Collections.emptyList();
        }
        return info;
    }

    // JWT 토큰의 유효성 + 만료일 체크
    public boolean validateToken(String token) {
        logger.info("[validateToken] 토큰 유효 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            logger.info("[validateToken] 토큰 유효 체크 완료");
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            logger.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }

}
