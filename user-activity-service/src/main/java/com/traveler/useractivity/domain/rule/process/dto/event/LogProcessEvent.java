package com.traveler.useractivity.domain.rule.process.dto.event;

import com.traveler.useractivity.global.cache.constant.CacheConstants;
import com.traveler.useractivity.global.cache.event.CacheEvictEvent;

public final class LogProcessEvent {

    private LogProcessEvent() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record Evict(Long logProcessId) implements CacheEvictEvent {

        @Override
        public String cacheName() {
            return CacheConstants.LOG_PROCESS_NAME;
        }

        @Override
        public Object cacheKey() {
            return logProcessId;
        }
    }
}
