package com.traveler.realtimepopular.controller;

import com.traveler.realtimepopular.dto.PopularScoreResult;
import com.traveler.realtimepopular.service.AggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/realtime-popular")
@RequiredArgsConstructor
public class SseController {
    private final AggregationService aggregationService;

    @GetMapping("/sse")
    public SseEmitter streamPopularBoards() {
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> {
            try {
                while (true) {
                    List<PopularScoreResult> top5 = aggregationService.getTopPopularBoards(5);
                    System.out.println("Send: " + top5);
                    emitter.send(top5);
                    Thread.sleep(10000); // 10초마다 전송
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }

}
