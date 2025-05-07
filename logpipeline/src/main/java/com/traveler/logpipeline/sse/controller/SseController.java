package com.traveler.logpipeline.sse.controller;

import com.traveler.logpipeline.sse.service.SseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController()
@RequestMapping("/sse")
public class SseController {
    private final SseService sseService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe(){
        return sseService.subscribe();
    }

    @PostMapping("/item")
    public String receiveItem(@RequestBody Map<String, String> item){
        return "Success";
    }


}
