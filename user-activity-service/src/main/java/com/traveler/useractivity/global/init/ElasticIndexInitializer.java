package com.traveler.useractivity.global.init;

import com.traveler.useractivity.domain.history.document.HistoryDocument;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticIndexInitializer {

    private final ElasticsearchOperations operations;

    @PostConstruct
    public void initIndexes() {
        // 초기화가 필요한 도메인 클래스들을 리스트에 추가
        List<Class<?>> documentClasses = List.of(HistoryDocument.class);

        for (Class<?> clazz : documentClasses) {
            setupIndex(clazz);
        }
    }

    private void setupIndex(Class<?> clazz) {
        IndexOperations indexOps = operations.indexOps(clazz);
        String name = indexOps.getIndexCoordinates().getIndexName();
        try {
            if (!indexOps.exists()) {
                log.info("Elasticsearch 인덱스가 존재하지 않습니다. 생성을 시작합니다: {}", name);
                indexOps.create();
                log.info("인덱스 생성 완료: {}", name);
            }
            indexOps.putMapping(indexOps.createMapping());
        } catch (Exception e) {
            log.error("Elasticsearch 인덱스 초기화 실패: {}", name, e);
            throw new IllegalStateException("Elasticsearch 인덱스 초기화 실패: " + name, e);
        }
    }
}
