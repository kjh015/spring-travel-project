package com.traveler.logpipeline.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.logpipeline.dto.FormatDto;
import com.traveler.logpipeline.entity.Format;
import com.traveler.logpipeline.repository.FormatRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/format")
public class FormatController {
    private final FormatRepository formatRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FormatController(FormatRepository formatRepository) {
        this.formatRepository = formatRepository;
    }

    @PostMapping("/config")
    public void updateFormatConfig(@RequestParam String processId, @RequestBody FormatDto formatConfig) throws JsonProcessingException {
        Format format = new Format();
        format.setProcessId(processId);
        format.setFormatJson(objectMapper.writeValueAsString(formatConfig.getConfig()));
        formatRepository.save(format);
    }

}
