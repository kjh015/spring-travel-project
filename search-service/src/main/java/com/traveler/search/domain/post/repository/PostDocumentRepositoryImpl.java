package com.traveler.search.domain.post.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@RequiredArgsConstructor
public class PostDocumentRepositoryImpl implements PostDocumentRepositoryCustom {
    private final ElasticsearchOperations operations;
}
