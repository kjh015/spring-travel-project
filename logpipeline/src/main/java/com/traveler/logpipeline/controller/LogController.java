package com.traveler.logpipeline.controller;

import com.traveler.logpipeline.dto.LogDto;
import com.traveler.logpipeline.service.LogFailByDeduplicationService;
import com.traveler.logpipeline.service.LogFailService;
import com.traveler.logpipeline.service.LogSuccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log-db")
@RequiredArgsConstructor
public class LogController {
    private final LogSuccessService logSuccessService;
    private final LogFailService logFailService;
    private final LogFailByDeduplicationService logFailByDeduplicationService;

    @GetMapping("/success")
    public List<LogDto> listSuccessLogs(){
        List<LogDto> log = logSuccessService.listSuccessLogs();
        System.out.println("-----------------------------");
        System.out.println("Success Log: ");
        System.out.println(log);
        System.out.println("-----------------------------");
        return log;
    }

    @GetMapping("/fail-filter")
    public List<LogDto> listFailLogs(){
        return logFailService.listFailLogs();
    }

    @GetMapping("/fail-deduplication")
    public List<LogDto> listFailDdpLogs(){
        return logFailByDeduplicationService.listFailDdpLogs();
    }


}
