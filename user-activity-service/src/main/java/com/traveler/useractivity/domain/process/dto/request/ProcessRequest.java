package com.traveler.useractivity.domain.process.dto.request;

public final class ProcessRequest {
    private ProcessRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO() {}
}
