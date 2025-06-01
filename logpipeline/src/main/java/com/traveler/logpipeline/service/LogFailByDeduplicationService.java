package com.traveler.logpipeline.service;

import com.traveler.logpipeline.dto.LogDto;
import com.traveler.logpipeline.entity.LogFailByDeduplication;
import com.traveler.logpipeline.repository.DeduplicationRepository;
import com.traveler.logpipeline.repository.LogFailByDeduplicationRepository;
import com.traveler.logpipeline.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogFailByDeduplicationService {
    private final LogFailByDeduplicationRepository logFailByDeduplicationRepository;
    private final DeduplicationRepository deduplicationRepository;
    private final ProcessRepository processRepository;

    public void addFailLog(LogFailByDeduplication log, Long processId, Long deduplicationId){
        log.setProcess(processRepository.findById(processId).orElse(null));
        log.setDeduplication(deduplicationRepository.findById(deduplicationId).orElse(null));
        logFailByDeduplicationRepository.save(log);
    }
    public List<LogDto> listFailDdpLogs(){
        return logFailByDeduplicationRepository.findAll().stream()
                .map(log -> LogDto.builder()
                        .id(log.getId())
                        .process(log.getProcess().getName())
                        .logJson(log.getLogJson())
                        .deduplication(log.getDeduplication().getName())
                        .createdTime(log.getCreatedTime()).build()
                )
                .collect(Collectors.toList());
    }
}
