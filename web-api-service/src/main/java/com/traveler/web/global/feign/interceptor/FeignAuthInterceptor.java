package com.traveler.web.global.feign.interceptor;

import com.traveler.common.api.auth.context.UserContextHolder;
import com.traveler.common.core.auth.AuthConstants;
import com.traveler.common.core.auth.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        UserContext context = UserContextHolder.getContext();

        if (context != null) {
            if (context.id() != null) {
                template.header(AuthConstants.X_USER_ID, String.valueOf(context.id()));
            }
            if (context.roles() != null && !context.roles().isEmpty()) {
                String rolesHeader = String.join(",", context.roles());
                template.header(AuthConstants.X_USER_ROLES, rolesHeader);
            }
            if (context.accessToken() != null && !context.accessToken().isBlank()) {
                template.header(AuthConstants.X_ACCESS_TOKEN, context.accessToken());
            }
        }
    }
}
