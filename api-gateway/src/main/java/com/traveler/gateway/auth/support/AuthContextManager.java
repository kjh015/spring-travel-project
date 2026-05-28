package com.traveler.gateway.auth.support;

import com.traveler.common.core.auth.AuthConstants;
import com.traveler.common.core.auth.UserContext;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class AuthContextManager {
    public static final String AUTH_USER_KEY = "auth_user_context";

    public ServerWebExchange prepareAuthorizedExchange(ServerWebExchange exchange, UserContext user) {
        return exchange.mutate()
                .request(r -> r.headers(headers -> {
                    // 상시 삭제 (보안 헤더 스푸핑 방지)
                    headers.remove(AuthConstants.X_USER_ID);
                    headers.remove(AuthConstants.X_USER_ROLES);
                    headers.remove(AuthConstants.X_ACCESS_TOKEN);

                    // 인증 정보가 있는 경우에만 주입 (Late Binding)
                    if (user != null) {
                        headers.add(AuthConstants.X_USER_ID, String.valueOf(user.id()));
                        if (user.roles() != null && !user.roles().isEmpty()) {
                            headers.add(AuthConstants.X_USER_ROLES, String.join(",", user.roles()));
                        }
                        if (user.accessToken() != null) {
                            headers.add(AuthConstants.X_ACCESS_TOKEN, user.accessToken());
                        }
                    }
                }))
                .build();
    }

    public void storeAuthenticatedUser(ServerWebExchange exchange, UserContext user) {
        if (user != null) {
            exchange.getAttributes().put(AUTH_USER_KEY, user);
        }
    }

    public Optional<UserContext> getAuthenticatedUser(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getAttribute(AUTH_USER_KEY));
    }
}
