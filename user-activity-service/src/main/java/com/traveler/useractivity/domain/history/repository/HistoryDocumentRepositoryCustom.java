package com.traveler.useractivity.domain.history.repository;

import com.traveler.useractivity.domain.history.document.HistoryDocument;
import com.traveler.useractivity.domain.history.enums.FailStage;
import com.traveler.useractivity.domain.history.enums.HistoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryDocumentRepositoryCustom {
    Page<HistoryDocument> findHistories(HistoryStatus status, FailStage stage, Pageable pageable);
}
