package com.traveler.common.api.auth.interceptor;

import com.traveler.common.api.auth.context.UserContextHolder;
import com.traveler.common.core.auth.AuthConstants;
import com.traveler.common.core.auth.UserContext;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader(AuthConstants.X_USER_ID);
        String role = request.getHeader(AuthConstants.X_USER_ROLES);

        if (userId != null && role != null) {
            try {
                List<String> roles = List.of(role.split(","));
                UserContext context = UserContext.of(Long.valueOf(userId), roles);
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
