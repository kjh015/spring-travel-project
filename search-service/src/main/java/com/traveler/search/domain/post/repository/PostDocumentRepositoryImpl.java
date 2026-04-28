package com.traveler.search.domain.post.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import com.traveler.search.domain.post.document.PostDocument;
import com.traveler.search.domain.post.dto.message.PostSearchMessage;
import com.traveler.search.domain.post.dto.request.PostSearchRequest;
import com.traveler.search.domain.post.enums.PostSortType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Slf4j
public class PostDocumentRepositoryImpl implements PostDocumentRepositoryCustom {
    private final ElasticsearchOperations operations;
    private final ElasticsearchClient client;

    @Value("${app.elasticsearch.indices.post}")
    private String postIndexName;

    private static final int BOOST_TITLE = 5;
    private static final int BOOST_TITLE_AUTOCOMPLETE = 3;
    private static final int BOOST_TITLE_NGRAM = 2;

    @Override
    public Page<PostDocument> search(PostSearchRequest.SearchDTO dto, Pageable pageable) {
        NativeQueryBuilder queryBuilder = new NativeQueryBuilder().withPageable(pageable);

        // 1. Must & Filter Query 구성
        queryBuilder.withQuery(q -> q.bool(b -> {
            buildMustClause(b, dto.keyword());
            buildFilterClause(b, dto.category(), dto.region());
            if (dto.sort() == PostSortType.ACCURACY) {
                b.should(s -> s.rankFeature(rf ->
                        rf.field(PostSortType.POPULAR.getField()).boost(2.0f).saturation(sa -> sa)));
            }
            return b;
        }));

        SearchHits<PostDocument> searchHits =
                operations.search(queryBuilder.build(), PostDocument.class, IndexCoordinates.of(postIndexName));
        return SearchHitSupport.searchPageFor(searchHits, pageable).map(SearchHit::getContent);
    }

    @Override
    public List<String> findAutocompleteTitles(String keyword) {
        NativeQueryBuilder queryBuilder = new NativeQueryBuilder();

        queryBuilder.withQuery(q -> q.bool(b -> {
            // 1. 일반 완성형 자동완성 (Edge-Ngram) - 높은 가중치
            b.should(s ->
                    s.prefix(p -> p.field("title.autocomplete").value(keyword).boost(5.0f)));

            // 2. 초성 자동완성 (Ngram) - 낮은 가중치
            b.should(s -> s.match(m -> m.field("titleChosung").query(keyword).analyzer("chosung_ngram_analyzer")));

            return b;
        }));

        // 3. 성능 최적화: 필요한 필드(title)만 fetch, 10개 제한
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[] {"title"}, null));
        queryBuilder.withMaxResults(10);

        SearchHits<PostDocument> searchHits =
                operations.search(queryBuilder.build(), PostDocument.class, IndexCoordinates.of(postIndexName));

        return searchHits.stream().map(hit -> hit.getContent().getTitle()).toList();
    }

    @Override
    @SneakyThrows
    public void updatePartial(PostSearchMessage.UpdatedDTO dto) {
        client.update(
                u -> u.index(postIndexName)
                        .id(String.valueOf(dto.postId()))
                        .doc(dto)
                        .retryOnConflict(3),
                PostSearchMessage.UpdatedDTO.class);
    }

    @SneakyThrows
    @Override
    public void updateStatistics(PostSearchMessage.StatUpdateDoc doc) {
        client.update(
                u -> u.index(postIndexName)
                        .id(String.valueOf(doc.postId()))
                        .doc(doc)
                        .retryOnConflict(3),
                PostSearchMessage.StatUpdateDoc.class);
    }

    private void buildMustClause(BoolQuery.Builder b, String keyword) {
        if (StringUtils.hasText(keyword)) {
            b.must(m -> m.multiMatch(mm -> mm.fields(
                            boost(PostDocument.Fields.TITLE, BOOST_TITLE),
                            boost(PostDocument.Fields.TITLE_AUTOCOMPLETE, BOOST_TITLE_AUTOCOMPLETE),
                            boost(PostDocument.Fields.TITLE_NGRAM, BOOST_TITLE_NGRAM),
                            PostDocument.Fields.CONTENT,
                            PostDocument.Fields.TRAVEL_PLACE,
                            PostDocument.Fields.ADDRESS)
                    .query(keyword)
                    .type(TextQueryType.MostFields)));
        } else {
            // 키워드가 없을 경우 match_all 추가
            b.must(m -> m.matchAll(ma -> ma));
        }
    }

    private void buildFilterClause(BoolQuery.Builder b, String category, String region) {
        if (StringUtils.hasText(category)) {
            b.filter(f -> f.term(t -> t.field("category").value(category)));
        }
        if (StringUtils.hasText(region)) {
            b.filter(f -> f.term(t -> t.field("region").value(region)));
        }
    }

    private String boost(String field, int val) {
        return field + "^" + val;
    }
}
