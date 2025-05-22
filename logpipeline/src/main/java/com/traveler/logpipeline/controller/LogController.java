package com.traveler.logpipeline.controller;

import com.traveler.logpipeline.entity.LogFail;
import com.traveler.logpipeline.entity.LogSuccess;
import com.traveler.logpipeline.service.LogFailService;
import com.traveler.logpipeline.service.LogSuccessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log-db")
public class LogController {
    private final LogSuccessService logSuccessService;
    private final LogFailService logFailService;

    public LogController(LogSuccessService logSuccessService, LogFailService logFailService) {
        this.logSuccessService = logSuccessService;
        this.logFailService = logFailService;
    }

    @GetMapping("/success")
    public List<LogSuccess> listSuccessLogs(){
        return logSuccessService.listSuccessLogs();
    }
    @GetMapping("/fail")
    public List<LogFail> listFailLogs(){
        return logFailService.listFailLogs();
    }
    @GetMapping("/success-process")
    public List<LogSuccess> listSuccessLogsByProcess(@RequestParam String processId){
        return logSuccessService.listSuccessLogsByProcess(Long.parseLong(processId));
    }
    @GetMapping("/fail-process")
    public List<LogFail> listFailLogsByProcess(@RequestParam String processId){
        return logFailService.listFailLogsByProcess(Long.parseLong(processId));
    }
    @GetMapping("/fail-filter")
    public List<LogFail> listFailLogsByFilter(@RequestParam String filterId){
        return logFailService.listFailLogsByFilter(Long.parseLong(filterId));
    }

}
