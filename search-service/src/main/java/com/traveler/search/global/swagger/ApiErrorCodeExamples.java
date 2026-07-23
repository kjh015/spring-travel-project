package com.traveler.search.global.swagger;

import com.traveler.common.api.swagger.annotation.SwaggerErrorResponse;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.search.global.exception.code.SearchServiceErrorCode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@SwaggerErrorResponse
public @interface ApiErrorCodeExamples {

    // ErrorCode
    ErrorCode[] value() default {};

    SearchServiceErrorCode[] search() default {};
}
