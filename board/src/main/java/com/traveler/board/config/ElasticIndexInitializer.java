package com.traveler.board.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ElasticIndexInitializer {

    private final ElasticsearchClient elasticsearchClient;

    public ElasticIndexInitializer(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @PostConstruct
    public void createBoardIndexIfNotExists() throws Exception {
        String indexName = "board";
        // 인덱스 존재 체크
        boolean exists = elasticsearchClient.indices().exists(r -> r.index(indexName)).value();
        if (exists) return;

        // nori analyzer가 적용된 인덱스 생성
        elasticsearchClient.indices().create(c -> c
                .index(indexName)
                .settings(s -> s
                        .analysis(a -> a
                                .analyzer("korean_nori", builder -> builder.nori(nori -> nori))
                        )
                )
                .mappings(m -> m
                        .properties("id", p -> p.long_(n -> n))
                        .properties("title", p -> p.text(t -> t.analyzer("korean_nori")))
                        .properties("content", p -> p.text(t -> t.analyzer("korean_nori")))
                        .properties("memberId", p -> p.long_(n -> n))
                        .properties("travelPlace", p -> p.text(t -> t.analyzer("korean_nori")))
                        .properties("address", p -> p.text(t -> t.analyzer("korean_nori")))
                        .properties("category", p -> p.text(t -> t.analyzer("korean_nori")))
                        .properties("region", p -> p.text(t -> t.analyzer("korean_nori")))
                        .properties("regDate", p -> p.date(d -> d))
                        .properties("modifiedDate", p -> p.date(d -> d))
                )

        );
    }
}
