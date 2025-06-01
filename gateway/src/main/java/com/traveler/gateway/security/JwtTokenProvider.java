package com.traveler.gateway.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtTokenProvider {


    @Value("${springboot.jwt.secret}")
    private String secretKey = "randomKey";

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsername(String token){
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public List<String> getRoles(String token){
        Object roles = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().get("roles");
        if (roles instanceof List<?>) {
            List<?> list = (List<?>) roles;
            List<String> info = new ArrayList<>();
            for(Object r : list) info.add(r.toString());
            return info;
        }
        return Collections.emptyList();
    }

    public Mono<String> resolveToken(ServerHttpRequest request) {
        List<String> authHeaders = request.getHeaders().getOrEmpty("Authorization");
        return Mono.justOrEmpty(
                authHeaders.stream()
                        .filter(h -> h.startsWith("Bearer "))
                        .map(h -> h.substring(7))
                        .findFirst()
                        .orElse(null)
        );
    }


    // JWT 토큰의 유효성 + 만료일 체크
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
