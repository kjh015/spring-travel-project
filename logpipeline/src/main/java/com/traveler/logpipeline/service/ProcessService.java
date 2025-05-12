package com.traveler.logpipeline.service;


import com.traveler.logpipeline.entity.Process;
import com.traveler.logpipeline.repository.ProcessRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessService {
    private final ProcessRepository processRepository;

    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    public List<Process> listProcesses(){
        return processRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
    public void addProcess(Process process){
        processRepository.save(process);
    }
    public void updateProcess(Process inProcess){
        Process process = processRepository.findById(inProcess.getId()).orElse(null);
        if(process != null){
            process.setName(inProcess.getName());
            processRepository.save(process);
        }
    }
    public void removeProcess(String processId){
        processRepository.deleteById(processId);
    }

}
//add, update, remove