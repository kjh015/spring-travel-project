package com.traveler.gateway.auth.filter;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.gateway.auth.support.AuthContextManager;
import com.traveler.gateway.exception.ApiGatewayNoStackException;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RoleAuthorizationFilter extends AbstractGatewayFilterFactory<RoleAuthorizationFilter.Config> {
    private final AuthContextManager authContextManager;

    public RoleAuthorizationFilter(AuthContextManager authContextManager) {
        super(Config.class);
        this.authContextManager = authContextManager;
    }

    @Data
    public static class Config {
        private String requiredRole; // 요구되는 권한 (예: ROLE_ADMIN)
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> authContextManager
                .getAuthenticatedUser(exchange)
                .map(authUser -> {
                    List<String> userRoles = authUser.roles();

                    // 권한 포함 여부 확인
                    if (userRoles == null || !userRoles.contains(config.getRequiredRole())) {
                        log.warn("[Auth] Access Denied. Required: {}, Actual: {}", config.getRequiredRole(), userRoles);
                        return Mono.<Void>error(new ApiGatewayNoStackException(ErrorCode.FORBIDDEN));
                    }

                    return chain.filter(exchange);
                })
                // 인증 정보 자체가 없는 경우 처리
                .orElseGet(() -> {
                    log.error(
                            "[Auth] AuthenticatedUser context missing for path: {}",
                            exchange.getRequest().getPath());
                    return Mono.error(new ApiGatewayNoStackException(ErrorCode.UNAUTHORIZED));
                });
    }
}
