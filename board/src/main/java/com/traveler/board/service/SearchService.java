package com.traveler.board.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.ScriptLanguage;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.traveler.board.dto.BoardDocumentDto;
import com.traveler.board.entity.Board;
import com.traveler.board.entity.BoardDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ElasticsearchClient elasticsearchClient;
    private final String indexName = "board-test5";
    private final int size = 10;

    public BoardDocumentDto search(String keyword, String category, String region, String sort, String direction, int page) {
        List<Query> filters = new ArrayList<>();
        if (category != null && !category.isEmpty()) {
            filters.add(Query.of(q -> q.term(t -> t.field("category").value(category))));
        }
        if (region != null && !region.isEmpty()) {
            filters.add(Query.of(q -> q.term(t -> t.field("region").value(region))));
        }

        Query mustQuery;
        if (keyword != null && !keyword.isEmpty()) {
            mustQuery = Query.of(m -> m.multiMatch(mm -> mm
                    .fields(
                            "title^5",         // 정확매칭
                            "title.autocomplete^3", // 자동완성
                            "title.ngram^2",   // 부분검색/오타
                            "content", "travelPlace", "address"
                    )
                    .query(keyword)
            ));
        } else {
            mustQuery = Query.of(m -> m.matchAll(ma -> ma));
        }

        SearchResponse<BoardDocument> response;
        try {
            response = elasticsearchClient.search(s -> {
                // 인기순일 때만 function_score 사용
                if ("popular".equalsIgnoreCase(sort)) {
                    // 필드 null 안전 방어 (size 체크) 추가
                    String script = """
                    0.5 * _score +
                    0.2 * (doc['viewCount'].size() > 0 ? doc['viewCount'].value : 0) +
                    0.15 * (doc['favoriteCount'].size() > 0 ? doc['favoriteCount'].value : 0) +
                    0.1 * (doc['commentCount'].size() > 0 ? doc['commentCount'].value : 0) +
                    0.05 * (doc['ratingAvg'].size() > 0 ? doc['ratingAvg'].value : 0)
                """;

                    return s
                            .index(indexName)
                            .query(q -> q.functionScore(fs -> fs
                                    .query(q2 -> q2.bool(b -> b
                                            .must(mustQuery)
                                            .filter(filters)
                                    ))
                                    .functions(fn -> fn
                                            .scriptScore(ss -> ss
                                                    .script(Script.of(sc -> sc
                                                            .inline(i -> i
                                                                    .lang(ScriptLanguage.Painless)
                                                                    .source(script)
                                                            )
                                                    ))
                                            )
                                    )
                                    .boostMode(FunctionBoostMode.Replace)
                            ))
                            .from(page * size)
                            .size(size);
                } else {
                    // 기존 정렬 방식 (정확도+필드 단일 정렬)
                    var builder = s
                            .index(indexName)
                            .query(q -> q.bool(b -> b
                                    .must(mustQuery)
                                    .filter(filters)
                            ))
                            .from(page * size)
                            .size(size);

                    if (sort != null && !sort.isBlank()) {
                        builder = builder.sort(so -> so
                                .field(f -> f
                                        .field(sort)
                                        .order("asc".equalsIgnoreCase(direction)
                                                ? SortOrder.Asc
                                                : SortOrder.Desc
                                        )
                                )
                        );
                    }
                    return builder;
                }
            }, BoardDocument.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<BoardDocument> result = response.hits().hits().stream()
                .map(Hit::source)
                .toList();
        long totalHits = 0L;
        if (response.hits().total() != null) {
            totalHits = response.hits().total().value();
        }

        return new BoardDocumentDto(result, totalHits);
    }


    public String getChosung(String str) {
        StringBuilder sb = new StringBuilder();
        for (char ch : str.toCharArray()) {
            if (ch >= 0xAC00 && ch <= 0xD7A3) {
                // 한글 음절을 초성 분리
                int unicode = ch - 0xAC00;
                int cho = unicode / (21 * 28);
                final char[] CHOSUNG = {
                        'ㄱ','ㄲ','ㄴ','ㄷ','ㄸ','ㄹ','ㅁ','ㅂ','ㅃ','ㅅ',
                        'ㅆ','ㅇ','ㅈ','ㅉ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'
                };
                sb.append(CHOSUNG[cho]);
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public List<String> autocomplete(String keyword) {
        SearchResponse<BoardDocument> response;
        try {
            response = elasticsearchClient.search(s -> s
                            .index(indexName)
                            .query(q -> q
                                    .prefix(p -> p
                                            .field("title.autocomplete")
                                            .value(keyword)
                                    )
                            )
                            .size(10),
                    BoardDocument.class
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 중복 제거
        return response.hits().hits().stream()
                .map(hit -> hit.source().getTitle())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> autocompleteChosung(String chosung) {
        SearchResponse<BoardDocument> response;
        try {
            response = elasticsearchClient.search(s -> s
                            .index(indexName)
                            .query(q -> q
                                    .match(m -> m
                                            .field("titleChosung")
                                            .query(chosung)
                                            .analyzer("chosung_ngram_analyzer")
                                    )
                            )
                            .size(10),
                    BoardDocument.class
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 중복 제거
        return response.hits().hits().stream()
                .map(hit -> hit.source().getTitle())
                .distinct()
                .collect(Collectors.toList());
    }

    public void updateComment(Board board){
        try {
            elasticsearchClient.update(
                    UpdateRequest.of(u -> u
                            .index(indexName)
                            .id(board.getId().toString())
                            .doc(Map.of(
                                    "ratingAvg", board.getRatingAvg(),
                                    "commentCount", board.getCommentCount()
                            ))

                    ),
                    BoardDocument.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateFavoriteCount(Board board){
        try {
            elasticsearchClient.update(
                    UpdateRequest.of(u -> u
                            .index(indexName)
                            .id(board.getId().toString())
                            .doc(Map.of("favoriteCount", board.getFavoriteCount()))
                    ),
                    BoardDocument.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateViewCount(Board board){
        try {
            elasticsearchClient.update(
                    UpdateRequest.of(u -> u
                            .index(indexName)
                            .id(board.getId().toString())
                            .doc(Map.of("viewCount", board.getViewCount()))
                    ),
                    BoardDocument.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateES(Board board){
        BoardDocument esDoc = BoardDocument.builder()
                .id(board.getId())
                .title(board.getTitle())
                .titleChosung(getChosung(board.getTitle()))
                .content(board.getContent())
                .memberId(board.getMemberId())
                .address(board.getTravelPlace().getAddress())
                .region(board.getTravelPlace().getRegion().getName())
                .travelPlace(board.getTravelPlace().getName())
                .category(board.getTravelPlace().getCategory().getName())
                .regDate(board.getRegDate())
                .modifiedDate(board.getModifiedDate())
                .ratingAvg(board.getRatingAvg())
                .commentCount(board.getCommentCount())
                .favoriteCount(board.getFavoriteCount())
                .viewCount(board.getViewCount())
                .build();

        try {
            elasticsearchClient.index(i -> i
                    .index(indexName) // 인덱스명
                    .id(esDoc.getId().toString())
                    .document(esDoc)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveToES(Board board) {
        BoardDocument esDoc = BoardDocument.builder()
                .id(board.getId())
                .title(board.getTitle())
                .titleChosung(getChosung(board.getTitle()))
                .content(board.getContent())
                .memberId(board.getMemberId())
                .address(board.getTravelPlace().getAddress())
                .region(board.getTravelPlace().getRegion().getName())
                .travelPlace(board.getTravelPlace().getName())
                .category(board.getTravelPlace().getCategory().getName())
                .regDate(board.getRegDate())
                .modifiedDate(board.getModifiedDate())
                .ratingAvg(0.0)
                .commentCount(0L)
                .favoriteCount(0L)
                .viewCount(0L)
                .build();

        try {
            elasticsearchClient.index(i -> i
                    .index(indexName) // 인덱스명
                    .id(esDoc.getId().toString())
                    .document(esDoc)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void migrateAllBoardsToES(List<Board> boardList) {
        List<BoardDocument> esDocs = boardList.stream().map(board -> BoardDocument.builder()
                .id(board.getId())
                .title(board.getTitle())
                .titleChosung(getChosung(board.getTitle()))
                .content(board.getContent())
                .memberId(board.getMemberId())
                .address(board.getTravelPlace().getAddress())
                .region(board.getTravelPlace().getRegion().getName())
                .travelPlace(board.getTravelPlace().getName())
                .category(board.getTravelPlace().getCategory().getName())
                .regDate(board.getRegDate())
                .modifiedDate(board.getModifiedDate())
                .ratingAvg(board.getRatingAvg() != null ? board.getRatingAvg() : 0.0)
                .commentCount(board.getCommentCount() != null ? board.getCommentCount() : 0L)
                .favoriteCount(board.getFavoriteCount() != null ? board.getFavoriteCount() : 0L)
                .viewCount(board.getViewCount() != null ? board.getViewCount() : 0L)
                .build()
        ).toList();

        // Bulk 인덱싱
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (BoardDocument doc : esDocs) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName) // 인덱스명
                            .id(doc.getId().toString())
                            .document(doc)
                    )
            );
        }

        try {
            BulkResponse result = elasticsearchClient.bulk(br.build());
            if (result.errors()) {
                // 에러 메시지 모으기
                StringBuilder sb = new StringBuilder("Elasticsearch bulk insert 오류:\n");
                result.items().stream()
                        .filter(item -> item.error() != null)
                        .forEach(item -> {
                            sb.append("- ID: ").append(item.id())
                                    .append(", 이유: ").append(item.error().reason())
                                    .append("\n");
                        });
                throw new CustomBoardException(sb.toString());
            }
        } catch (IOException e) {
            throw new CustomBoardException("Elasticsearch bulk insert 중 IOException 발생" + e.getMessage());
        }

    }

    public void deleteById(Long boardId) {
        try {
            elasticsearchClient.delete(d -> d
                    .index(indexName)
                    .id(boardId.toString())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
