package com.traveler.search.global.config;

import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.util.StringUtils;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.traveler.search.domain")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String esUris;

    // ※ 이 클래스가 ElasticsearchConfiguration을 상속해 ClientConfiguration을 직접 만들기 때문에
    //    Spring Boot의 ES 자동 설정은 물러난다(back off). 즉 application.yml에
    //    spring.elasticsearch.username/password 를 적어두기만 해서는 인증이 적용되지 않는다.
    //    반드시 아래처럼 값을 읽어 withBasicAuth()로 넘겨야 한다.
    @Value("${spring.elasticsearch.username:}")
    private String esUsername;

    @Value("${spring.elasticsearch.password:}")
    private String esPassword;

    private final ObjectMapper objectMapper;

    public ElasticsearchConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ClientConfiguration clientConfiguration() {
        ClientConfiguration.TerminalClientConfigurationBuilder builder = ClientConfiguration.builder()
                .connectedTo(esUris)
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(10));

        // ES에 xpack.security가 켜져 있을 때만 Basic 인증을 붙인다.
        // username/password 는 반드시 쌍으로 설정한다. 한쪽만 설정되어 있으면
        // 인증이 조용히 누락되거나 인증 실패가 런타임까지 지연되므로 기동 단계에서 실패시킨다.
        boolean hasUsername = StringUtils.hasText(esUsername);
        boolean hasPassword = StringUtils.hasText(esPassword);
        if (hasUsername != hasPassword) {
            throw new IllegalStateException("Elasticsearch 자격 증명이 불완전합니다. "
                    + "spring.elasticsearch.username 과 spring.elasticsearch.password 는 "
                    + "함께 설정하거나 함께 비워두어야 합니다.");
        }

        // 두 값이 모두 비어 있으면(보안 미적용 환경) 인증 없이 접속한다.
        if (hasUsername) {
            builder = builder.withBasicAuth(esUsername, esPassword);
        }

        return builder.build();
    }

    @Override
    public JsonpMapper jsonpMapper() {
        return new JacksonJsonpMapper(objectMapper);
    }
}
