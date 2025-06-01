package com.traveler.logpipeline.service;

import com.traveler.logpipeline.dto.LogDto;
import com.traveler.logpipeline.entity.LogSuccess;
import com.traveler.logpipeline.repository.LogSuccessRepository;
import com.traveler.logpipeline.repository.ProcessRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogSuccessService {
    private final LogSuccessRepository logSuccessRepository;
    private final ProcessRepository processRepository;

    public LogSuccessService(LogSuccessRepository logSuccessRepository, ProcessRepository processRepository) {
        this.logSuccessRepository = logSuccessRepository;
        this.processRepository = processRepository;
    }

    public void addSuccessLog(LogSuccess log, Long processId){
        log.setProcess(processRepository.findById(processId).orElse(null));
        logSuccessRepository.save(log);
    }
    public List<LogDto> listSuccessLogs(){
        return logSuccessRepository.findAll().stream()
                .map(log -> LogDto.builder()
                        .id(log.getId())
                        .process(log.getProcess().getName())
                        .logJson(log.getLogJson())
                        .createdTime(log.getCreatedTime()).build()
                )
                .collect(Collectors.toList());
    }
    public List<LogSuccess> listSuccessLogsByProcess(Long processId){
        return logSuccessRepository.findAllByProcess_Id(processId);
    }

}
