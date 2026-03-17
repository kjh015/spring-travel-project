package com.traveler.common.api.swagger.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String securitySchemeName = "JWT TOKEN";

    /** OpenAPI 객체의 공통 설정을 담당하는 커스텀 로직 */
    public OpenApiCustomizer createOpenApiCustomizer(String title, String version, String description) {
        return openApi -> {
            openApi.info(new Info().title(title).version(version).description(description));
            openApi.setServers(List.of(new Server().url("/")));
            openApi.addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
            openApi.schemaRequirement(securitySchemeName, createBearerAuthScheme());
            openApi.getComponents().addSchemas("ApiResponse", new Schema<>()
                    .type("object")
                    .addProperty("success", new Schema<>().type("boolean"))
                    .addProperty("code", new Schema<>().type("string"))
                    .addProperty("message", new Schema<>().type("string"))
                    .addProperty("result", new Schema<>().type("object").nullable(true))
            );
        };
    }

    /** JWT Bearer 인증 스키마 생성 */
    private SecurityScheme createBearerAuthScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name(securitySchemeName);
    }
}
