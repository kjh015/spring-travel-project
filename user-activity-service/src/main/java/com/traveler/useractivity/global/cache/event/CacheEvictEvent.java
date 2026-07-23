package com.traveler.useractivity.global.cache.event;

public interface CacheEvictEvent {
    String cacheName();

    Object cacheKey();
}
