package com.traveler.useractivity.domain.history.service;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.history.document.HistoryDocument;
import com.traveler.useractivity.domain.history.dto.response.HistoryResponse;
import com.traveler.useractivity.domain.history.mapper.HistoryDocumentMapper;
import com.traveler.useractivity.domain.history.repository.HistoryDocumentRepository;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryQueryService {
    private final HistoryDocumentRepository historyDocumentRepository;
    private final HistoryDocumentMapper historyDocumentMapper;

    public PageResponse<HistoryResponse.SuccessDTO> getSuccessHistories(Pageable pageable) {
        Page<HistoryDocument> pageData = historyDocumentRepository.findHistories(true, null, pageable);
        return PageConverter.toPageResponse(pageData, historyDocumentMapper::toSuccessDTO);
    }

    public PageResponse<HistoryResponse.FailDTO> getFailByFilterHistories(Pageable pageable) {
        Page<HistoryDocument> pageData = historyDocumentRepository.findHistories(false, "FILTER", pageable);
        return PageConverter.toPageResponse(pageData, historyDocumentMapper::toFailDTO);
    }

    public PageResponse<HistoryResponse.FailDTO> getFailByDedupHistories(Pageable pageable) {
        Page<HistoryDocument> pageData = historyDocumentRepository.findHistories(false, "DEDUP", pageable);
        return PageConverter.toPageResponse(pageData, historyDocumentMapper::toFailDTO);
    }

    public HistoryResponse.DetailDTO getHistory(String historyId) {
        HistoryDocument historyDocument = historyDocumentRepository
                .findById(historyId)
                .orElseThrow(() -> new UserActivityServiceException(UserActivityServiceErrorCode.HISTORY_NOT_FOUND));

        return historyDocumentMapper.toDetailDTO(historyDocument);
    }
}
