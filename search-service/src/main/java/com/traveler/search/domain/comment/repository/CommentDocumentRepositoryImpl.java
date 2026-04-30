package com.traveler.search.domain.comment.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.traveler.search.domain.comment.dto.message.CommentSearchMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@RequiredArgsConstructor
public class CommentDocumentRepositoryImpl implements CommentDocumentRepositoryCustom {
    private final ElasticsearchOperations operations;
    private final ElasticsearchClient client;

    @Value("${app.elasticsearch.indices.comment}")
    private String commentIndexName;

    @SneakyThrows
    @Override
    public void updatePartial(CommentSearchMessage.UpdatedDTO dto) {
        client.update(
                u -> u.index(commentIndexName)
                        .id(String.valueOf(dto.commentId()))
                        .doc(dto)
                        .retryOnConflict(3),
                CommentSearchMessage.UpdatedDTO.class);
    }
}
