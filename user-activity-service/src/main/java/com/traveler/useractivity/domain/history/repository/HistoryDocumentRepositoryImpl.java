package com.traveler.useractivity.domain.history.repository;

import com.traveler.useractivity.domain.history.document.HistoryDocument;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

@RequiredArgsConstructor
public class HistoryDocumentRepositoryImpl implements HistoryDocumentRepositoryCustom {
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<HistoryDocument> findHistories(boolean success, String stage, Pageable pageable) {
        // 1. 성공/실패 조건 (공통)
        Criteria criteria = new Criteria(HistoryDocument.Fields.SUCCESS).is(success);

        // 2. 실패일 경우 스테이지 조건 동적 추가 (동적 쿼리 패턴)
        if (stage != null && !stage.isBlank()) {
            criteria.and(new Criteria(HistoryDocument.Fields.ERROR_STAGE).is(stage));
        }

        // 3. 쿼리 생성 및 페이징 세팅
        CriteriaQuery query = new CriteriaQuery(criteria).setPageable(pageable);

        // 4. ES 조회 실행
        SearchHits<HistoryDocument> searchHits = elasticsearchOperations.search(query, HistoryDocument.class);

        // 5. Spring Data Domain Page 객체로 변환
        List<HistoryDocument> content =
                searchHits.stream().map(SearchHit::getContent).toList();

        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }
}
