package com.traveler.useractivity.domain.process.sink.dto.event;

import com.traveler.useractivity.domain.process.sink.dto.message.SinkMessage;

public final class SinkEvent {

    private SinkEvent() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record PostViewed(SinkMessage.PostViewedDTO message) {
        public static final String EVENT_TYPE = "POST_VIEWED";
    }
}
