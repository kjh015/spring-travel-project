package com.traveler.common.api.swagger.config;

import com.traveler.common.api.auth.resolver.LoginUser;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(LoginUser.class);
    }

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    /** OpenAPI 객체의 공통 설정을 담당하는 커스텀 로직 */
    public OpenApiCustomizer createOpenApiCustomizer(String title, String version, String description) {
        return openApi -> {
            openApi.info(new Info().title(title).version(version).description(description));
            openApi.setServers(List.of(new Server().url("/")));
            openApi.addSecurityItem(new SecurityRequirement()
                    .addList(USER_ID_HEADER)
                    .addList(USER_ROLE_HEADER));
            openApi.getComponents()
                    .addSecuritySchemes(USER_ID_HEADER, createHeaderScheme(USER_ID_HEADER))
                    .addSecuritySchemes(USER_ROLE_HEADER, createHeaderScheme(USER_ROLE_HEADER));
            openApi.getComponents()
                    .addSchemas(
                            "ApiResponse",
                            new Schema<>()
                                    .type("object")
                                    .addProperty("success", new Schema<>().type("boolean"))
                                    .addProperty("code", new Schema<>().type("string"))
                                    .addProperty("message", new Schema<>().type("string"))
                                    .addProperty(
                                            "result",
                                            new Schema<>().type("object").nullable(true)));
        };
    }

    /** 헤더 입력을 위한 API Key 스키마 생성 */
    private SecurityScheme createHeaderScheme(String name) {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(name);
    }
}
