package com.traveler.useractivity.domain.history.dto.response;

import java.time.Instant;
import java.util.Map;

public final class HistoryResponse {
    private HistoryResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record SuccessDTO(String historyId, String logProcessName, Instant createdAt) {}

    public record FailDTO(
            String historyId, String logProcessName, String failStage, String failRuleName, Instant createdAt) {}

    public record DetailDTO(String historyId, Map<String, Object> logData) {}
}
