package com.traveler.realtimepopular.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.traveler.realtimepopular.dto.PopularScore;
import com.traveler.realtimepopular.dto.PopularScoreResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AggregationService {
    private final ElasticsearchClient esClient;

    // boardNo별로 집계 후, 가중치 연산해서 반환
    public Map<String, PopularScore> getPopularBoards() throws IOException {
        // 1. Aggregation 쿼리 생성
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("logstash-*")
                .size(0)
                .aggregations("by_board", a -> a
                        .terms(t -> t.field("게시판 번호.keyword").size(30))
                        .aggregations("views", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_action").value("view"))))
                        .aggregations("favorites", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_action").value("favorite"))))
                        .aggregations("comments", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_action").value("comment"))))
                )
        );

        // 2. 쿼리 실행
        SearchResponse<Void> res = esClient.search(searchRequest, Void.class);

        // 3. 결과 파싱
        Map<String, PopularScore> result = new HashMap<>();

        Aggregate agg = res.aggregations().get("by_board");
        if (agg != null && agg.isSterms()) { // terms 집계
            var byBoardAgg = agg.sterms();
            for (var bucket : byBoardAgg.buckets().array()) {
                String boardNo = bucket.key().stringValue();

                // 하위 agg에서 집계값 파싱
                long views = getFilterDocCount(bucket, "views");
                long likes = getFilterDocCount(bucket, "likes");
                long comments = getFilterDocCount(bucket, "comments");

                // 가중치 공식 (원하는 대로 수정)
                long score = views * 1 + likes * 3 + comments * 2;

                result.put(boardNo, new PopularScore(views, likes, comments, score));
            }
        }

        return result;
    }

    // Filter 집계에서 docCount 추출
    private long getFilterDocCount(StringTermsBucket bucket, String key) {
        Aggregate aggr = bucket.aggregations().get(key);
        if (aggr != null && aggr.isFilter()) {
            return aggr.filter().docCount();
        }
        return 0;
    }

    public List<PopularScoreResult> getTopPopularBoards(int topN) throws IOException {
        Map<String, PopularScore> all = getPopularBoards(); // 기존 집계 Map

        // 1. Map의 entrySet을 stream으로 변환
        return all.entrySet().stream()
                // 2. 점수 내림차순 정렬
                .sorted((a, b) -> Long.compare(b.getValue().getScore(), a.getValue().getScore()))
                // 3. 상위 N개만 남기기
                .limit(topN)
                // 4. 결과 객체로 변환 (boardNo, score만 포함)
                .map(entry -> new PopularScoreResult(entry.getKey(), entry.getValue().getScore()))
                .toList();
    }
}
