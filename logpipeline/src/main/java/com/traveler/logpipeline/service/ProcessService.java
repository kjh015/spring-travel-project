package com.traveler.logpipeline.service;


import com.traveler.logpipeline.dto.ProcessDto;
import com.traveler.logpipeline.entity.Process;
import com.traveler.logpipeline.repository.ProcessRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessService {
    private final ProcessRepository processRepository;

    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    public List<ProcessDto> listProcesses(){
        return processRepository.findAll().stream()
                .map(process -> ProcessDto.builder()
                        .id(process.getId())
                        .name(process.getName())
                        .createdTime(process.getCreatedTime())
                        .updatedTime(process.getUpdatedTime()).build()
                )
                .collect(Collectors.toList());
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
    public void removeProcess(Long processId){
        processRepository.deleteById(processId);
    }

}
//add, update, remove