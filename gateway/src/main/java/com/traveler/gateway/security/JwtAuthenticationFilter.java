package com.traveler.gateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider jwtTokenProvider;
    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        if (path.startsWith("/api/sign/sign-in") || path.startsWith("/api/sign/sign-up")
                || path.startsWith("/api/sign/refresh")
                || path.startsWith("/api/board/list") || path.startsWith("/api/board/view")
                || path.startsWith("/api/favorite/exists")
                || path.startsWith("/api/comment/list")
                || path.startsWith("/realtime-popular/")
                || path.startsWith("/board/images/")
        ) {
            logger.info("Authentication Pass: {}", path);
            return chain.filter(exchange); // 인증 안 거침
        }
        // 아래에서 JWT 검사, 실패시 401/403 응답
        return jwtTokenProvider.resolveToken(exchange.getRequest())
                .filter(jwtTokenProvider::validateToken)
                .flatMap(token -> {
                    logger.info("인증 시작: {}", path);
                    logger.info("토큰: {}", token);
                    List<String> roles = jwtTokenProvider.getRoles(token);
                    if (roles.isEmpty() || roles.stream().noneMatch(r -> r.equals("ROLE_USER") || r.equals("ROLE_ADMIN"))) {
                        logger.info("인증실패 - 유효하지 않은 권한 403: {}", roles);
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                    logger.info("인증 성공: {}", path);
                    return chain.filter(exchange); // 통과
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.info("인증실패 - 유효하지 않은 인증 401");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }));
    }
    @Override
    public int getOrder() { return -1; }
}
