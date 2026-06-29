package com.traveler.web.domain.useractivity.client.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;

public final class HistoryClientResponse {
    private HistoryClientResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ListDTO(
            String historyId,
            String logProcessName,
            boolean success,
            String failStage,
            String failRuleName,
            Instant createdAt) {}

    public record DetailDTO(String historyId, Map<String, Object> logData) {}
}
