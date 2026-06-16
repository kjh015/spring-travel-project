package com.traveler.useractivity.domain.deduplication.dto.request;

public final class DeduplicationRequest {
    private DeduplicationRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO() {}
}
