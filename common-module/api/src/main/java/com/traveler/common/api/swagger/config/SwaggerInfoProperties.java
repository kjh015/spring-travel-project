package com.traveler.common.api.swagger.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "swagger.info")
@Getter
@Setter
public class SwaggerInfoProperties {
    private String title = "API Documentation";
    private String version = "v1.0";
    private String description = "API description";
    private String basePackage;
    private List<String> pathsToMatch = List.of("/api/**");
}
