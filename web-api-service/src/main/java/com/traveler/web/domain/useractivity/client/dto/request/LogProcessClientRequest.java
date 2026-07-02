package com.traveler.web.domain.useractivity.client.dto.request;

public final class LogProcessClientRequest {
    private LogProcessClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(String name, String description) {}

    public record UpdateDTO(String name, String description) {}
}
