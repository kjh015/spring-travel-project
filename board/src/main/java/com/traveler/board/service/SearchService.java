package com.traveler.board.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.traveler.board.entity.Board;
import com.traveler.board.entity.BoardDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ElasticsearchClient elasticsearchClient;
    private final String indexName = "board";

    public List<BoardDocument> search(String keyword)  {
        SearchResponse<BoardDocument> response = null;
        try {
            response = elasticsearchClient.search(s -> s
                            .index(indexName)
                            .query(q -> q
                                    .multiMatch(m -> m
                                            .fields("title", "content", "travelPlace", "address")
                                            .query(keyword)
                                    )
                            ),
                    BoardDocument.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();
    }

    public void saveToES(Board board) {
        BoardDocument esDoc = BoardDocument.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .memberId(board.getMemberId())
                .address(board.getTravelPlace().getAddress())
                .region(board.getTravelPlace().getRegion().getName())
                .travelPlace(board.getTravelPlace().getName())
                .category(board.getTravelPlace().getCategory().getName())
                .regDate(board.getRegDate())
                .modifiedDate(board.getModifiedDate())
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
                .content(board.getContent())
                .memberId(board.getMemberId())
                .address(board.getTravelPlace().getAddress())
                .region(board.getTravelPlace().getRegion().getName())
                .travelPlace(board.getTravelPlace().getName())
                .category(board.getTravelPlace().getCategory().getName())
                .regDate(board.getRegDate())
                .modifiedDate(board.getModifiedDate())
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






}
