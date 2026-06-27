package com.traveler.useractivity.domain.history.repository;

import com.traveler.useractivity.domain.history.document.HistoryDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryDocumentRepositoryCustom {
    Page<HistoryDocument> findHistories(boolean success, String stage, Pageable pageable);
}
