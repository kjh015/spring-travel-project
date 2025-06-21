package com.traveler.monitoring.controller;

import com.traveler.monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monitoring")
@RequiredArgsConstructor
public class MonitoringController {
    private final MonitoringService monitoringService;

    @PostMapping("/top")
    public ResponseEntity<?> getTop(@RequestParam String data){
        System.out.println("getTop Start");
        try{
            return ResponseEntity.ok().body(monitoringService.getTop(data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/visit")
    public ResponseEntity<List<Map<String, Object>>> getPageVisitStats(@RequestParam String period) {
        // period: "hour", "day", "month"
        try{
            return ResponseEntity.ok().body(monitoringService.getPageVisitStats(period));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
