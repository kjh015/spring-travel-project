package com.traveler.logpipeline.sse.controller;

import com.traveler.logpipeline.kafka.service.KafkaProducerService;
import com.traveler.logpipeline.sse.service.SseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController()
@RequestMapping("/sse")
public class SseController {
    private final SseService sseService;
    private final KafkaProducerService kafkaProducerService;

    public SseController(SseService sseService, KafkaProducerService kafkaProducerService) {
        this.sseService = sseService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe(){
        return sseService.subscribe();
    }

    @PostMapping("/item")
    public String receiveItem(@RequestBody Map<String, String> item){
        kafkaProducerService.sendMessageToKafka("FILTER_TOPIC", item);
        return "Success";
    }


}
