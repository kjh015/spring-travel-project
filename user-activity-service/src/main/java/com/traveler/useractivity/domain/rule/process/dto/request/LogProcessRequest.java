package com.traveler.useractivity.domain.rule.process.dto.request;

public final class LogProcessRequest {
    private LogProcessRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(String name, String description) {}

    public record UpdateDTO(String name, String description) {}
}
