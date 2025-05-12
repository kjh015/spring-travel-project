package com.traveler.logpipeline.controller;

import com.traveler.logpipeline.service.ProcessService;
import org.springframework.web.bind.annotation.*;
import com.traveler.logpipeline.entity.Process;


import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {
    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping("/list")
    public List<Process> getProcessList(){
        return processService.listProcesses();

    }
    @PostMapping("/add")
    public void addProcess(@RequestParam String name){
        Process process = new Process();
        process.setName(name);
        processService.addProcess(process);
    }
    @PostMapping("/update")
    public void updateProcess(@RequestParam String processId, @RequestParam String name){
        Process process = new Process();
        process.setId(Long.parseLong(processId));
        process.setName(name);
        processService.updateProcess(process);
    }
    @PostMapping("/remove")
    public void removeProcess(@RequestParam String processId){
        processService.removeProcess(Long.parseLong(processId));
    }
}
