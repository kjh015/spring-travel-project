package com.traveler.web.global.feign.config;

import com.traveler.web.global.feign.decoder.FeignErrorDecoder;
import com.traveler.web.global.feign.interceptor.FeignAuthInterceptor;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignAuthInterceptor();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
