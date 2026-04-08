package com.traveler.common.api.config;

import com.traveler.common.api.converter.ExceptionConverter;
import com.traveler.common.api.handler.GeneralExceptionAdvice;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        GeneralExceptionAdvice.class,
        ExceptionConverter.class
})
public class CommonApiConfig {
}