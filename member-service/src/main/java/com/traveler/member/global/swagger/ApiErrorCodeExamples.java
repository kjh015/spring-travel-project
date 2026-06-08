package com.traveler.member.global.swagger;

import com.traveler.common.api.swagger.annotation.SwaggerErrorResponse;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.member.global.exception.code.MemberServiceErrorCode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@SwaggerErrorResponse
public @interface ApiErrorCodeExamples {

    MemberServiceErrorCode[] value() default {};

    ErrorCode[] common() default {};
}
