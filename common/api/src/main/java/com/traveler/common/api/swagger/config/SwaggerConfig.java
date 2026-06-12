package com.traveler.common.api.swagger.config;

import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.auth.AuthConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import java.util.Optional;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(LoginUser.class);
    }

    private static final String BEARER_TOKEN_PREFIX = "JWT Token";

    /** OpenAPI 객체의 공통 설정을 담당하는 커스텀 로직 */
    public OpenApiCustomizer createOpenApiCustomizer(String title, String version, String description) {
        return openApi -> {
            openApi.info(new Info().title(title).version(version).description(description));
            openApi.setServers(List.of(new Server().url("/")));
            openApi.addSecurityItem(new SecurityRequirement()
                    .addList(BEARER_TOKEN_PREFIX)
                    .addList(AuthConstants.X_USER_ID)
                    .addList(AuthConstants.X_USER_ROLES)
                    .addList(AuthConstants.X_ACCESS_TOKEN));
            Components components = Optional.ofNullable(openApi.getComponents()).orElseGet(Components::new);
            openApi.setComponents(components);
            components
                    .addSecuritySchemes(BEARER_TOKEN_PREFIX, createBearerScheme())
                    .addSecuritySchemes(AuthConstants.X_USER_ID, createHeaderScheme(AuthConstants.X_USER_ID))
                    .addSecuritySchemes(AuthConstants.X_USER_ROLES, createHeaderScheme(AuthConstants.X_USER_ROLES))
                    .addSecuritySchemes(AuthConstants.X_ACCESS_TOKEN, createHeaderScheme(AuthConstants.X_ACCESS_TOKEN));
            components.addSchemas(
                    "ApiResponse",
                    new Schema<>()
                            .type("object")
                            .addProperty("success", new Schema<>().type("boolean"))
                            .addProperty("code", new Schema<>().type("string"))
                            .addProperty("message", new Schema<>().type("string"))
                            .addProperty("result", new Schema<>().type("object").nullable(true)));
        };
    }
    /** JWT Bearer 인증을 위한 스키마 생성 */
    private SecurityScheme createBearerScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(AuthConstants.AUTHORIZATION_HEADER);
    }

    /** 헤더 입력을 위한 API Key 스키마 생성 */
    private SecurityScheme createHeaderScheme(String name) {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(name);
    }
}
