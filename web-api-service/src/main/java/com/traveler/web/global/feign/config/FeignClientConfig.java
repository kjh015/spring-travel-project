package com.traveler.web.global.feign.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.web.global.feign.decoder.FeignErrorDecoder;
import com.traveler.web.global.feign.decoder.ValidatedFeignDecoder;
import com.traveler.web.global.feign.interceptor.FeignAuthInterceptor;
import feign.QueryMapEncoder;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.optionals.OptionalDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.PageableSpringQueryMapEncoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class FeignClientConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;
    private final ObjectMapper objectMapper;

    @Bean
    public Decoder feignDecoder() {
        Decoder defaultDecoder = new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)));
        return new ValidatedFeignDecoder(defaultDecoder);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignAuthInterceptor();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder(objectMapper);
    }

    @Bean
    public QueryMapEncoder queryMapEncoder() {
        return new PageableSpringQueryMapEncoder();
    }
}
