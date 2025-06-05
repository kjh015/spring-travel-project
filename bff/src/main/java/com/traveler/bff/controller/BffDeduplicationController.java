package com.traveler.bff.controller;

import com.traveler.bff.client.LogpipelineServiceClient;
import com.traveler.bff.dto.service.DeduplicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deduplication")
@RequiredArgsConstructor
public class BffDeduplicationController {
    private final LogpipelineServiceClient logpipelineServiceClient;

    @GetMapping("/list")
    public List<DeduplicationDto> getDeduplicationList(@RequestParam String processId) {
        return logpipelineServiceClient.getDeduplicationList(processId);
    }

    @GetMapping("/view")
    public DeduplicationDto viewDeduplication(@RequestParam String deduplicationId) {
        return logpipelineServiceClient.viewDeduplication(deduplicationId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDeduplication(@RequestBody DeduplicationDto data) {
        System.out.println("Data: ");
        System.out.println(data);
        return logpipelineServiceClient.addDeduplication(data);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateDeduplication(@RequestBody DeduplicationDto data) {
        return logpipelineServiceClient.updateDeduplication(data);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeDeduplication(@RequestParam String deduplicationId) {
        return logpipelineServiceClient.removeDeduplication(deduplicationId);
    }

    @GetMapping("/keys")
    public List<String> getFormatList(@RequestParam String processId) {
        return logpipelineServiceClient.getFormatFieldsD(processId);
    }
}