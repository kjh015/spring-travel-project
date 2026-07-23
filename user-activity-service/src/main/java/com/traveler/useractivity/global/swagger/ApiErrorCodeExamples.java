package com.traveler.useractivity.global.swagger;

import com.traveler.common.api.swagger.annotation.SwaggerErrorResponse;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@SwaggerErrorResponse
public @interface ApiErrorCodeExamples {

    UserActivityServiceErrorCode[] value() default {};

    ErrorCode[] common() default {};
}
