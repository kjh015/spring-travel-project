package com.traveler.search.domain.comment.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.traveler.search.domain.comment.document.CommentDocument;
import com.traveler.search.domain.comment.dto.message.CommentSearchMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

@RequiredArgsConstructor
public class CommentDocumentRepositoryImpl implements CommentDocumentRepositoryCustom {
    private final ElasticsearchOperations operations;
    private final ElasticsearchClient client;

    private IndexCoordinates commentIndex;

    @PostConstruct
    public void init() {
        this.commentIndex = operations.getIndexCoordinatesFor(CommentDocument.class);
    }

    @SneakyThrows
    @Override
    public void updatePartial(CommentSearchMessage.UpdatedDTO dto) {
        client.update(
                u -> u.index(commentIndex.getIndexName())
                        .id(String.valueOf(dto.commentId()))
                        .doc(dto)
                        .retryOnConflict(3),
                CommentSearchMessage.UpdatedDTO.class);
    }
}
