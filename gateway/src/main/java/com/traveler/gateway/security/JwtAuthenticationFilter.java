package com.traveler.gateway.security;

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

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        if (path.startsWith("/sign-api/sign-in") ||
                path.startsWith("/sign-api/sign-up") ||
                path.startsWith("/sign-api/refresh")) {
            return chain.filter(exchange); // 인증 안 거침
        }
        // 아래에서 JWT 검사, 실패시 401/403 응답
        return jwtTokenProvider.resolveToken(exchange.getRequest())
                .filter(jwtTokenProvider::validateToken)
                .flatMap(token -> {
                    List<String> roles = jwtTokenProvider.getRoles(token);
                    if (roles.isEmpty() || roles.stream().noneMatch(r -> r.equals("ROLE_USER") || r.equals("ROLE_ADMIN"))) {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                    return chain.filter(exchange); // 통과
                })
                .switchIfEmpty(Mono.defer(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }));
    }
    @Override
    public int getOrder() { return -1; }
}
