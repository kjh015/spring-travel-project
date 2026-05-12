package com.traveler.gateway.auth.filter;

import com.traveler.common.core.auth.AuthConstants;
import com.traveler.common.core.auth.UserContext;
import com.traveler.gateway.auth.support.AuthContextManager;
import com.traveler.gateway.auth.support.JwtTokenProvider;
import com.traveler.gateway.auth.support.TokenBlacklistManager;
import com.traveler.gateway.exception.ApiGatewayErrorCode;
import com.traveler.gateway.exception.ApiGatewayNoStackException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthContextManager authContextManager;
    private final TokenBlacklistManager tokenBlacklistManager;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            AuthContextManager authContextManager,
            TokenBlacklistManager tokenBlacklistManager) {
        super(Config.class);
        this.jwtTokenProvider = jwtTokenProvider;
        this.authContextManager = authContextManager;
        this.tokenBlacklistManager = tokenBlacklistManager;
    }

    @Data
    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> Mono.justOrEmpty(resolveToken(exchange.getRequest()))
                .switchIfEmpty(Mono.error(new ApiGatewayNoStackException(ApiGatewayErrorCode.INVALID_TOKEN_TYPE)))
                .flatMap(token -> tokenBlacklistManager.checkBlacklist(token).then(Mono.just(token)))
                .flatMap(jwtTokenProvider::validateToken)
                .<UserContext>handle((claims, sink) -> {
                    try {
                        Long userId = Long.valueOf(claims.getSubject());
                        sink.next(UserContext.of(userId, jwtTokenProvider.getRoles(claims)));
                    } catch (NumberFormatException e) {
                        sink.error(new ApiGatewayNoStackException(ApiGatewayErrorCode.INVALID_TOKEN_TYPE));
                    }
                })
                .flatMap(authUser -> {
                    // 컨텍스트 저장 및 헤더 주입
                    authContextManager.storeAuthenticatedUser(exchange, authUser);
                    return chain.filter(authContextManager.prepareAuthorizedExchange(exchange, authUser));
                });
    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AuthConstants.BEARER_PREFIX)) {
            return bearerToken.substring(AuthConstants.BEARER_PREFIX.length());
        }
        return null;
    }
}
