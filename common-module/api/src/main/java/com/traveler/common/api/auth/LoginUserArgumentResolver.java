package com.traveler.common.api.auth;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.exception.GeneralException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @LoginUser 어노테이션이 붙어있고, 타입이 AuthenticatedUser인 경우
        return parameter.hasParameterAnnotation(LoginUser.class)
                && parameter.getParameterType().equals(UserContext.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {

        String userId = webRequest.getHeader("X-User-Id");
        String role = webRequest.getHeader("X-User-Role");

        // 인증이 필수인 API인데 헤더가 없다면 401 예외 발생
        if (userId == null || role == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }

        try {
            return new UserContext(Long.valueOf(userId), role);
        } catch (NumberFormatException e) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
    }
}
