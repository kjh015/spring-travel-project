package com.traveler.bff.controller;

import com.traveler.bff.client.LogpipelineServiceClient;
import com.traveler.bff.dto.service.FilterRequestDto;
import com.traveler.bff.dto.service.FilterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filter")
@RequiredArgsConstructor
public class BffFilterController {
    private final LogpipelineServiceClient logpipelineServiceClient;

    @GetMapping("/list")
    public List<FilterResponseDto> getFilterList(@RequestParam String processId) {
        return logpipelineServiceClient.getFilterList(processId);
    }

    @GetMapping("/view")
    public FilterResponseDto viewFilter(@RequestParam String filterId) {
        return logpipelineServiceClient.viewFilter(filterId);
    }

    @GetMapping("/keys")
    public List<String> getFormatList(@RequestParam String processId) {
        return logpipelineServiceClient.getFormatFieldsF(processId);
    }

    @PostMapping("/add")
    public String addFilter(@RequestParam String processId, @RequestParam String name, @RequestParam String active, @RequestBody FilterRequestDto data) {
        return logpipelineServiceClient.addFilter(processId, name, active, data);
    }

    @PostMapping("/update")
    public String updateFilter(@RequestParam String filterId, @RequestParam String name, @RequestParam String active, @RequestBody FilterRequestDto data) {
        return logpipelineServiceClient.updateFilter(filterId, name, active, data);
    }

    @PostMapping("/remove")
    public String removeFilter(@RequestParam String filterId) {
        return logpipelineServiceClient.removeFilter(filterId);
    }
}
