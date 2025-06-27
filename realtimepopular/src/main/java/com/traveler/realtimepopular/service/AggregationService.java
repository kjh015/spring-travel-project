package com.traveler.realtimepopular.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AggregationService {
    private final ElasticsearchClient esClient;

    // boardNo별로 집계 후, 가중치 연산해서 반환
    public Map<String, Long> getPopularBoards() throws IOException {
        // 실존 boardID 추출
        Set<String> aliveBoardIds = getAliveBoardIds();
        // 1. Aggregation 쿼리 생성
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("logstash-*")
                .size(0)
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m.term(t -> t.field("success").value(true)))
                        )
                )
                .aggregations("by_board", a -> a
                        .terms(t -> t.field("게시판 번호.keyword").size(30))
                        .aggregations("views_board", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("페이지 방문"))))
                        .aggregations("add_favorites_board", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("찜 추가"))))
                        .aggregations("add_comments_board", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("댓글 추가"))))
                        .aggregations("rm_favorites_board", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("찜 삭제"))))
                        .aggregations("rm_comments_board", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("댓글 삭제"))))
                        .aggregations("stay_time_sum_board", sub -> sub.sum(sum -> sum.field("Stay_Time")))

                )
        );

        // 2. 쿼리 실행
        SearchResponse<Void> res = esClient.search(searchRequest, Void.class);
        // 3. 결과 파싱
        Map<String, Long> result = new HashMap<>();

        Aggregate agg = res.aggregations().get("by_board");
        if (agg != null && agg.isSterms()) { // terms 집계
            var byBoardAgg = agg.sterms();
            for (var bucket : byBoardAgg.buckets().array()) {
                String boardNo = bucket.key().stringValue();
                // 삭제된 boardID는 skip
                if (!aliveBoardIds.contains(boardNo)) continue;

                // 하위 agg에서 집계값 파싱
                long views = getFilterDocCount(bucket, "views_board");
                long addFavorites = getFilterDocCount(bucket, "add_favorites_board");
                long addComments = getFilterDocCount(bucket, "add_comments_board");
                long removeFavorites = getFilterDocCount(bucket, "rm_favorites_board");
                long removeComments = getFilterDocCount(bucket, "rm_comments_board");
                double stayTimeSum = 0.0;
                Aggregate stayAgg = bucket.aggregations().get("stay_time_sum_board");
                if (stayAgg != null && stayAgg.isSum()) {
                    stayTimeSum = stayAgg.sum().value();
                }

//                System.out.printf("view: %d, fav: %d, comment: %d, stay: %.0f\n", views, favorites, comments, stayTimeSum);

                // 가중치 공식 (예시: 체류시간 1초 = 0.1점)
                long score = (long)(views * 1 + addFavorites * 3 + addComments * 2 + stayTimeSum * 0.1 - removeFavorites * 2.5 - removeComments * 1.5);
                System.out.printf("boardId: %s, score: %d\n", boardNo, score);
                System.out.println();
//                if(boardNo.equals("10")) score -= 350;
//                if(boardNo.equals("11")) score -= 100;

                result.put(boardNo, score);
            }
        }
        return result;
    }

    public Map<String, Long> getPopularCategories() throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("logstash-*")
                .size(0)
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m.term(t -> t.field("success").value(true)))
                                .mustNot(m -> m.term(t -> t.field("카테고리.keyword").value("없음")))
                        )
                )
                .aggregations("by_category", a -> a
                        .terms(t -> t.field("카테고리.keyword").size(30))
                        .aggregations("views_category", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("페이지 방문"))))
                        .aggregations("add_favorites_category", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("찜 추가"))))
                        .aggregations("add_comments_category", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("댓글 추가"))))
                        .aggregations("rm_favorites_category", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("찜 삭제"))))
                        .aggregations("rm_comments_category", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("댓글 삭제"))))
                        .aggregations("search_category", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("검색 클릭"))))
                )
        );
        SearchResponse<Void> res = esClient.search(searchRequest, Void.class);
        Map<String, Long> result = new HashMap<>();

        Aggregate agg = res.aggregations().get("by_category");
        if (agg != null && agg.isSterms()) {
            var byCategoryAgg = agg.sterms();
            for (var bucket : byCategoryAgg.buckets().array()) {
                String category = bucket.key().stringValue();

                long views = getFilterDocCount(bucket, "views_category");
                long addFavorites = getFilterDocCount(bucket, "add_favorites_category");
                long addComments = getFilterDocCount(bucket, "add_comments_category");
                long removeFavorites = getFilterDocCount(bucket, "rm_favorites_category");
                long removeComments = getFilterDocCount(bucket, "rm_comments_category");
                long search = getFilterDocCount(bucket, "search_category");

                long score = (long)(views * 1 + search * 4 + addFavorites * 3 + addComments * 2 - removeFavorites * 2.5 - removeComments * 1.5);

                result.put(category, score);
            }
        }

        return result;
    }

    public Map<String, Long> getPopularRegions() throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("logstash-*")
                .size(0)
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m.term(t -> t.field("success").value(true)))
                                .mustNot(m -> m.term(t -> t.field("지역.keyword").value("없음")))
                        )
                )
                .aggregations("by_region", a -> a
                        .terms(t -> t.field("지역.keyword").size(30))
                        .aggregations("views_region", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("페이지 방문"))))
                        .aggregations("add_favorites_region", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("찜 추가"))))
                        .aggregations("add_comments_region", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("댓글 추가"))))
                        .aggregations("rm_favorites_region", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("찜 삭제"))))
                        .aggregations("rm_comments_region", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("댓글 삭제"))))
                        .aggregations("search_region", sub -> sub.filter(fq -> fq.term(tq -> tq.field("event_name.keyword").value("검색 클릭"))))
                )
        );
        SearchResponse<Void> res = esClient.search(searchRequest, Void.class);
        Map<String, Long> result = new HashMap<>();

        Aggregate agg = res.aggregations().get("by_region");
        if (agg != null && agg.isSterms()) {
            var byCategoryAgg = agg.sterms();
            for (var bucket : byCategoryAgg.buckets().array()) {
                String category = bucket.key().stringValue();

                long views = getFilterDocCount(bucket, "views_region");
                long addFavorites = getFilterDocCount(bucket, "add_favorites_region");
                long addComments = getFilterDocCount(bucket, "add_comments_region");
                long removeFavorites = getFilterDocCount(bucket, "rm_favorites_region");
                long removeComments = getFilterDocCount(bucket, "rm_comments_region");
                long search = getFilterDocCount(bucket, "search_region");

                long score = (long)(views * 1 + search * 4 + addFavorites * 3 + addComments * 2 - removeFavorites * 2.5 - removeComments * 1.5);

                result.put(category, score);
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

    public double getBoardRatingAvg(String boardId) throws IOException {
        SearchRequest request = SearchRequest.of(s -> s
                .index("board-test5") // board 인덱스명
                .query(q -> q.term(t -> t.field("id").value(boardId)))
                .size(1)
                .source(src -> src.filter(f -> f.includes("ratingAvg"))) // ratingAvg 필드만
        );
        SearchResponse<Map> res = esClient.search(request, Map.class);

        // 1건만 조회했으니, 첫번째 결과에서 ratingAvg 추출
        if (res.hits().hits().isEmpty()) return 0.0;

        Map doc = res.hits().hits().get(0).source();
        if (doc == null) return 0.0;

        Object val = doc.get("ratingAvg");
        if (val instanceof Number) return ((Number) val).doubleValue();

        if (val instanceof String) {
            try { return Double.parseDouble((String) val); }
            catch (Exception e) { return 0.0; }
        }
        return 0.0;
    }

    // 1. board 인덱스에서 boardId만 뽑아오기
    public Set<String> getAliveBoardIds() throws IOException {
        SearchRequest req = SearchRequest.of(s -> s
                .index("board-test5")
                .source(src -> src.filter(f -> f.includes("id")))
                .size(10000) // 충분히 크게 (운영이라면 Scroll API)
        );
        SearchResponse<Map> res = esClient.search(req, Map.class);

        Set<String> boardIds = new HashSet<>();
        res.hits().hits().forEach(hit -> {
            Map doc = hit.source();
            if(doc == null) return;
            Object id = doc.get("id");
            if (id != null) boardIds.add(String.valueOf(id));
        });
        return boardIds;
    }






}
