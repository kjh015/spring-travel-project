package com.traveler.gateway.auth.filter;

import com.traveler.common.core.constant.AuthConstants;
import com.traveler.gateway.auth.context.AuthenticatedUser;
import com.traveler.gateway.auth.support.AuthContextManager;
import com.traveler.gateway.auth.support.JwtTokenProvider;
import com.traveler.gateway.auth.support.TokenBlacklistManager;
import com.traveler.gateway.exception.ApiGatewayErrorCode;
import com.traveler.gateway.exception.ApiGatewayNoStackException;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
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
    public static class Config {
        private List<String> excludePaths;
    }

    @Override
    public GatewayFilter apply(Config config) {
        List<PathPattern> patterns = parsePatterns(config.getExcludePaths());

        return (exchange, chain) -> {
            RequestPath path = exchange.getRequest().getPath();

            // 제외 경로 확인
            if (isExcluded(path, patterns)) {
                return chain.filter(authContextManager.prepareAuthorizedExchange(exchange, null));
            }

            return Mono.justOrEmpty(resolveToken(exchange.getRequest()))
                    .switchIfEmpty(Mono.error(new ApiGatewayNoStackException(ApiGatewayErrorCode.INVALID_TOKEN_TYPE)))
                    .flatMap(
                            token -> tokenBlacklistManager.checkBlacklist(token).then(Mono.just(token)))
                    .flatMap(jwtTokenProvider::validateToken)
                    .map(claims -> AuthenticatedUser.of(claims.getSubject(), jwtTokenProvider.getRoles(claims)))
                    .flatMap(authUser -> {
                        // 컨텍스트 저장 및 헤더 주입
                        authContextManager.storeAttributes(exchange, authUser);
                        return chain.filter(authContextManager.prepareAuthorizedExchange(exchange, authUser));
                    });
        };
    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AuthConstants.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private List<PathPattern> parsePatterns(List<String> paths) {
        if (paths == null) return Collections.emptyList();
        PathPatternParser parser = new PathPatternParser();
        return paths.stream().map(parser::parse).toList();
    }

    private boolean isExcluded(RequestPath path, List<PathPattern> patterns) {
        return patterns.stream().anyMatch(pattern -> pattern.matches(path));
    }
}
