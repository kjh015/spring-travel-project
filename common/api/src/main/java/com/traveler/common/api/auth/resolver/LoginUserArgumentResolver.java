package com.traveler.common.api.auth.resolver;

import com.traveler.common.api.auth.context.UserContextHolder;
import com.traveler.common.core.auth.UserContext;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.exception.GeneralException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @LoginUser 어노테이션이 붙어있고, 타입이 UserContext 경우
        return parameter.hasParameterAnnotation(LoginUser.class)
                && parameter.getParameterType().equals(UserContext.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {

        LoginUser annotation = parameter.getParameterAnnotation(LoginUser.class);
        UserContext context = UserContextHolder.getContext();

        // 어노테이션에서 required = true(기본값)인데 유저 정보가 없으면 예외 발생
        if (annotation != null && annotation.required() && context == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }

        return context;
    }
}
