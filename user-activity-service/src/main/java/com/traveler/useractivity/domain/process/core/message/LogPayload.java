package com.traveler.useractivity.domain.process.core.message;

import java.time.Instant;

public record LogPayload<T>(LogMetadata metadata, boolean success, FailInfo failInfo, Instant timestamp, T data) {

    public static <T> LogPayload<T> success(LogMetadata metadata, T data) {
        return new LogPayload<>(metadata, true, null, Instant.now(), data);
    }

    public static <T> LogPayload<T> failure(LogMetadata metadata, FailInfo failInfo, T data) {
        return new LogPayload<>(metadata, false, failInfo, Instant.now(), data);
    }
}
