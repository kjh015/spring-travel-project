package com.traveler.sign.security;


import com.traveler.sign.service.MemberDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private final MemberDetailsService memberDetailsService;

    public JwtTokenProvider(MemberDetailsService memberDetailsService) {
        this.memberDetailsService = memberDetailsService;
    }
    private Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${springboot.jwt.secret}")
    private String secretKey = "randomKey";
    private final long tokenValidMillisecond = 1000L * 60 * 60;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String loginId, List<String> roles){
        logger.info("[createToken] 토큰 생성 시작");
        Date now = new Date();
        String token = Jwts.builder().subject(loginId).claim("roles", roles).issuedAt(now)
                .expiration(new Date(now.getTime() + tokenValidMillisecond)).signWith(getSigningKey()).compact();
        logger.info("[createToken] 토큰 생성 완료");
        return token;
    }

    public Authentication getAuthentication(String token){
        logger.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        UserDetails userDetails = memberDetailsService.loadUserByUsername(this.getUsername(token));
        logger.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails UserName : {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token){
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;
    }

    public String resolveToken(HttpServletRequest request) {
        logger.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
        return request.getHeader("X-AUTH-TOKEN");
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
