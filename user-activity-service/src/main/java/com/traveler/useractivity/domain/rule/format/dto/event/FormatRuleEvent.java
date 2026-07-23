package com.traveler.useractivity.domain.rule.format.dto.event;

import com.traveler.useractivity.global.cache.constant.CacheConstants;
import com.traveler.useractivity.global.cache.event.CacheEvictEvent;

public final class FormatRuleEvent {

    private FormatRuleEvent() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record Evict(Long logProcessId) implements CacheEvictEvent {

        @Override
        public String cacheName() {
            return CacheConstants.ACTIVE_FORMAT_RULES;
        }

        @Override
        public Object cacheKey() {
            return logProcessId;
        }
    }
}
