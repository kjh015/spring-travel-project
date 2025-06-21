package com.traveler.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "monitoring")
public interface MonitoringServiceClient {
    @PostMapping("/monitoring/top")
    ResponseEntity<?> getTop(@RequestParam String data);
    @PostMapping("/monitoring/visit")
    ResponseEntity<List<Map<String, Object>>> getPageVisitStats(@RequestParam String period);
}
