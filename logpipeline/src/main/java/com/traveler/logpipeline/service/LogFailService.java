package com.traveler.logpipeline.service;

import com.traveler.logpipeline.dto.LogDto;
import com.traveler.logpipeline.entity.LogFail;
import com.traveler.logpipeline.repository.FilterRepository;
import com.traveler.logpipeline.repository.LogFailRepository;
import com.traveler.logpipeline.repository.ProcessRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogFailService {
    private final LogFailRepository logFailRepository;
    private final ProcessRepository processRepository;
    private final FilterRepository filterRepository;

    public LogFailService(LogFailRepository logFailRepository, ProcessRepository processRepository, FilterRepository filterRepository) {
        this.logFailRepository = logFailRepository;
        this.processRepository = processRepository;
        this.filterRepository = filterRepository;
    }

    public void addFailLog(LogFail log, Long processId, Long filterId){
        log.setProcess(processRepository.findById(processId).orElse(null));
        log.setFilter(filterRepository.findById(filterId).orElse(null));
        logFailRepository.save(log);
    }
    public List<LogDto> listFailLogs(){
        return logFailRepository.findAll().stream()
                .map(log -> LogDto.builder()
                        .id(log.getId())
                        .process(log.getProcess().getName())
                        .logJson(log.getLogJson())
                        .filter(log.getFilter().getName())
                        .createdTime(log.getCreatedTime()).build()
                )
                .collect(Collectors.toList());
    }




    public List<LogFail> listFailLogsByProcess(Long processId){
        return logFailRepository.findAllByProcess_Id(processId);
    }
    public List<LogFail> listFailLogsByFilter(Long filterId){
        return logFailRepository.findAllByFilter_Id(filterId);
    }
    public LogFail viewFailLog(Long logId){
        return logFailRepository.findById(logId).orElse(null);
    }





}
