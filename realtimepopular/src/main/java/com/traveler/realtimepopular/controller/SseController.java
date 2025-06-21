package com.traveler.realtimepopular.controller;

import com.traveler.realtimepopular.dto.PopularScoreResult;
import com.traveler.realtimepopular.service.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/realtime-popular")
@RequiredArgsConstructor
public class SseController {
    private final PopularService popularService;

    @GetMapping("/sse")
    public SseEmitter streamPopularBoards() {
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> {
            try {
                while (true) {
                    List<PopularScoreResult> top5Boards = popularService.getTopPopularBoards(5);
                    List<PopularScoreResult> top5Categories = popularService.getTopPopularCategories(5);
                    List<PopularScoreResult> top5Regions = popularService.getTopPopularRegions(5);

                    Map<String, Object> data = new HashMap<>();
                    data.put("top5Boards", top5Boards);
                    data.put("top5Categories", top5Categories);
                    data.put("top5Regions", top5Regions);

                    System.out.println("Send: " + data);
                    emitter.send(data);
                    Thread.sleep(10000); // 10초마다 전송
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }

}
