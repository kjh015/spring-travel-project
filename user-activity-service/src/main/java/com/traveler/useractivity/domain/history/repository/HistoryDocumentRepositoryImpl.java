package com.traveler.useractivity.domain.history.repository;

import com.traveler.useractivity.domain.history.document.HistoryDocument;
import com.traveler.useractivity.domain.history.enums.FailStage;
import com.traveler.useractivity.domain.history.enums.HistoryStatus;
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
    public Page<HistoryDocument> findHistories(HistoryStatus status, FailStage stage, Pageable pageable) {
        Criteria criteria = new Criteria();

        // 1. 상태 조건 동적 추가 (null이면 전체 조회)
        if (status != null) {
            boolean isSuccess = (status == HistoryStatus.SUCCESS);
            criteria.and(new Criteria(HistoryDocument.Fields.SUCCESS).is(isSuccess));
        }

        // 2. 실패 스테이지 조건 동적 추가
        if (stage != null) {
            criteria.and(new Criteria(HistoryDocument.Fields.FAIL_INFO_STAGE).is(stage.name()));
        }

        CriteriaQuery query = new CriteriaQuery(criteria).setPageable(pageable);
        SearchHits<HistoryDocument> searchHits = elasticsearchOperations.search(query, HistoryDocument.class);

        List<HistoryDocument> content =
                searchHits.stream().map(SearchHit::getContent).toList();
        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }
}
