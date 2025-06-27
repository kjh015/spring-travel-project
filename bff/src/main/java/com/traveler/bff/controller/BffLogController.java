package com.traveler.bff.controller;

import com.traveler.bff.client.LogpipelineServiceClient;
import com.traveler.bff.dto.service.LogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/log-db/admin")
@RequiredArgsConstructor
public class BffLogController {
    private final LogpipelineServiceClient logpipelineServiceClient;

    @GetMapping("/success")
    public List<LogDto> listSuccessLogs() {
        List<LogDto> log = logpipelineServiceClient.listSuccessLogs();
        System.out.println("-----------------------------");
        System.out.println("Success Log: ");
        System.out.println(log);
        System.out.println("-----------------------------");
        return log;
    }

    @GetMapping("/fail-filter")
    public List<LogDto> listFailLogs() {
        return logpipelineServiceClient.listFailLogs();
    }

    @GetMapping("/fail-deduplication")
    public List<LogDto> listFailDdpLogs() {
        return logpipelineServiceClient.listFailDdpLogs();
    }
}