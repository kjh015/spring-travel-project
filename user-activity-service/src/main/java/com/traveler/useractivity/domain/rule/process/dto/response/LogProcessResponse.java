package com.traveler.useractivity.domain.rule.process.dto.response;

import java.time.Instant;

public final class LogProcessResponse {
    private LogProcessResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(Long logProcessId, Instant createdAt) {}

    public record UpdateDTO(Long logProcessId, Instant updatedAt) {}

    public record DeleteDTO(Long logProcessId, Instant deletedAt) {}

    public record ListDTO(Long logProcessId, String name, String description, Instant createdAt, Instant updatedAt) {}
}
