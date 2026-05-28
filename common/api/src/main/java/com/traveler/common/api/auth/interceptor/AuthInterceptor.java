package com.traveler.common.api.auth.interceptor;

import com.traveler.common.api.auth.context.UserContextHolder;
import com.traveler.common.core.auth.AuthConstants;
import com.traveler.common.core.auth.UserContext;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader(AuthConstants.X_USER_ID);
        String roles = request.getHeader(AuthConstants.X_USER_ROLES);
        String accessToken = request.getHeader(AuthConstants.X_ACCESS_TOKEN);

        if (userId != null && roles != null) {
            try {
                List<String> roleList =
                        Arrays.stream(roles.split(",")).map(String::trim).toList();
                UserContext context = UserContext.of(Long.valueOf(userId), roleList, accessToken);
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
