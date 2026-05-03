package com.traveler.web.global.feign.interceptor;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.context.UserContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        UserContext context = UserContextHolder.getContext();

        if (context != null) {
            template.header("X-User-Id", String.valueOf(context.id()));
            template.header("X-User-Role", context.role());
        }
    }
}
