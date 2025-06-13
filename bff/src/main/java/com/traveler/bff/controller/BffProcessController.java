package com.traveler.bff.controller;

import com.traveler.bff.client.LogpipelineServiceClient;
import com.traveler.bff.dto.service.ProcessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/process/admin")
@RequiredArgsConstructor
public class BffProcessController {
    private final LogpipelineServiceClient logpipelineServiceClient;

    @GetMapping("/list")
    public List<ProcessDto> getProcessList() {
        return logpipelineServiceClient.getProcessList();
    }

    @PostMapping("/add")
    public void addProcess(@RequestParam String name) {
        logpipelineServiceClient.addProcess(name);
    }

    @PostMapping("/update")
    public void updateProcess(@RequestParam String processId, @RequestParam String name) {
        logpipelineServiceClient.updateProcess(processId, name);
    }

    @PostMapping("/remove")
    public void removeProcess(@RequestParam String processId) {
        logpipelineServiceClient.removeProcess(processId);
    }
}