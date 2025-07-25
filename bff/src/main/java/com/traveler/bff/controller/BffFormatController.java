package com.traveler.bff.controller;

import com.traveler.bff.client.LogpipelineServiceClient;
import com.traveler.bff.dto.service.FormatRequestDto;
import com.traveler.bff.dto.service.FormatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/format/admin")
@RequiredArgsConstructor
public class BffFormatController {
    private final LogpipelineServiceClient logpipelineServiceClient;

    @GetMapping("/list")
    public List<FormatResponseDto> getFormatList(@RequestParam String processId) {
        return logpipelineServiceClient.getFormatList(processId);
    }

    @GetMapping("/view")
    public FormatResponseDto viewFormat(@RequestParam String formatId) {
        return logpipelineServiceClient.viewFormat(formatId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFormat(@RequestParam String processId, @RequestParam String name, @RequestParam String active, @RequestBody FormatRequestDto formatData) {
        return logpipelineServiceClient.addFormat(processId, name, active, formatData);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateFormat(@RequestParam String formatId, @RequestParam String name, @RequestParam String active, @RequestBody FormatRequestDto formatData) {
        return logpipelineServiceClient.updateFormat(formatId, name, active, formatData);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFormat(@RequestParam String formatId) {
        return logpipelineServiceClient.removeFormat(formatId);
    }
}