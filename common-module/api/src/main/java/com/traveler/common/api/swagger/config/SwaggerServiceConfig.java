package com.traveler.common.api.swagger.config;

import com.traveler.common.api.swagger.annotation.ApiErrorCodeOperationCustomizer;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

@Configuration
@Import({SwaggerConfig.class})
@EnableConfigurationProperties(SwaggerInfoProperties.class)
@RequiredArgsConstructor
public class SwaggerServiceConfig {

    private final SwaggerInfoProperties properties;
    private final ApiErrorCodeOperationCustomizer apiErrorCodeOperationCustomizer;
    private final SwaggerConfig swaggerConfig; // 기존 공통 로직 재사용

    @Bean
    public ApiErrorCodeOperationCustomizer apiErrorCodeOperationCustomizer() {
        return new ApiErrorCodeOperationCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean // 서비스에서 직접 정의한 Bean이 없을 때만 자동 생성
    public GroupedOpenApi autoGroupedOpenApi(Environment env) {
        // 서비스 이름 가져오기 (예: post-service)
        String appName = env.getProperty("spring.application.name", "default-service");

        // 제목이 비어있으면 서비스 이름을 활용해 자동 생성
        String title = (properties.getTitle() == null || properties.getTitle().equals("API Documentation"))
                ? appName.toUpperCase() + " API"
                : properties.getTitle();

        String[] pathsToMatch = (properties.getPathsToMatch() == null
                        || properties.getPathsToMatch().isEmpty())
                ? new String[] {"/**"}
                : properties.getPathsToMatch().toArray(String[]::new);

        return GroupedOpenApi.builder()
                .group(appName)
                .pathsToMatch(pathsToMatch)
                .addOpenApiCustomizer(swaggerConfig.createOpenApiCustomizer(
                        title, properties.getVersion(), appName + " 관리 API 명세서입니다."))
                .addOperationCustomizer(apiErrorCodeOperationCustomizer)
                .build();
    }
}
