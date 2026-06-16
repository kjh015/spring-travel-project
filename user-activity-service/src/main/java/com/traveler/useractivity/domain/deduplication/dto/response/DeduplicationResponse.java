package com.traveler.useractivity.domain.deduplication.dto.response;

public final class DeduplicationResponse {
    private DeduplicationResponse() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO() {}
}
