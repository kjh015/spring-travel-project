package com.traveler.logpipeline.service;

import com.traveler.logpipeline.entity.LogSave;
import com.traveler.logpipeline.repository.LogSaveRepository;
import com.traveler.logpipeline.repository.ProcessRepository;
import org.springframework.stereotype.Service;

@Service
public class LogSaveService {
    private final LogSaveRepository logSaveRepository;
    private final ProcessRepository processRepository;

    public LogSaveService(LogSaveRepository logSaveRepository, ProcessRepository processRepository) {
        this.logSaveRepository = logSaveRepository;
        this.processRepository = processRepository;
    }

    public void addLog(LogSave log, Long processId){
        log.setProcess(processRepository.findById(processId).orElse(null));
        logSaveRepository.save(log);

    }
    public void removeLog(){

    }

}
