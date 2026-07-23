package com.traveler.useractivity.domain.history.repository;

import com.traveler.useractivity.domain.history.document.HistoryDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface HistoryDocumentRepository
        extends ElasticsearchRepository<HistoryDocument, String>, HistoryDocumentRepositoryCustom {}
