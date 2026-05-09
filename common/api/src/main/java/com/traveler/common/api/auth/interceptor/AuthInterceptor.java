package com.traveler.common.api.auth.interceptor;

import com.traveler.common.api.auth.context.UserContext;
import com.traveler.common.api.auth.context.UserContextHolder;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-User-Role");

        if (userId != null && role != null) {
            try {
                UserContext context = new UserContext(Long.valueOf(userId), role);
                UserContextHolder.setContext(context);
            } catch (NumberFormatException e) {
                throw new GeneralException(ErrorCode.BAD_REQUEST);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextHolder.clear();
    }
}
