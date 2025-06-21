package com.traveler.bff.controller;

import com.traveler.bff.client.MonitoringServiceClient;
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
@RequestMapping("/api/monitoring/admin")
@RequiredArgsConstructor
public class BffMonitoringController {
    private final MonitoringServiceClient monitoringServiceClient;

    @PostMapping("/top")
    public ResponseEntity<?> getTop(@RequestParam String data){
        System.out.println("getTop Start");
        try{
            return ResponseEntity.ok().body(monitoringServiceClient.getTop(data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/visit")
    public ResponseEntity<List<Map<String, Object>>> getPageVisitStats(@RequestParam String period) {
        try{
            return ResponseEntity.ok().body(monitoringServiceClient.getPageVisitStats(period).getBody());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
