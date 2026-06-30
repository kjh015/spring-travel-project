package com.traveler.useractivity.domain.process.core.provider;

import com.traveler.useractivity.domain.rule.process.entity.LogProcess;
import com.traveler.useractivity.domain.rule.process.repository.LogProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogProcessProvider {
    private final LogProcessRepository logProcessRepository;

    @Cacheable(cacheNames = "logProcessNameCache", key = "#logProcessId")
    public String getLogProcessName(Long logProcessId) {
        return logProcessRepository
                .findById(logProcessId)
                .map(LogProcess::getName)
                .orElse("Unknown Process");
    }
}
