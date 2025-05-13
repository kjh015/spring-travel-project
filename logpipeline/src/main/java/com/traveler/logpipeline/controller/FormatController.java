package com.traveler.logpipeline.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.logpipeline.dto.FormatDto;
import com.traveler.logpipeline.entity.Format;
import com.traveler.logpipeline.service.FormatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/format")
public class FormatController {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FormatService formatService;

    public FormatController(FormatService formatService) {
        this.formatService = formatService;
    }

    @GetMapping("/list")
    public List<Format> getFormatList(@RequestParam String processId){
        return formatService.listFormats(Long.parseLong(processId));
    }
    @GetMapping("/view")
    public Format viewFormat(@RequestParam String formatId){
        return formatService.viewFormat(Long.parseLong(formatId));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFormat(@RequestParam String processId, @RequestParam String name, @RequestParam String active, @RequestBody FormatDto formatData)  {
        try {
            Format format = new Format();
            format.setName(name);
            format.setDefaultJson(objectMapper.writeValueAsString(formatData.getDefaultInfo()));
            format.setFormatJson(objectMapper.writeValueAsString(formatData.getFormatInfo()));
            format.setActive(Boolean.parseBoolean(active));
            formatService.addFormat(format, Long.parseLong(processId));
            return ResponseEntity.ok("포맷 저장 성공");
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("JSON 파싱 실패: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateFormat(@RequestParam String formatId, @RequestParam String name, @RequestParam String active, @RequestBody FormatDto formatData){
        try{
            Format format = new Format();
            format.setId(Long.parseLong(formatId));
            format.setName(name);
            format.setDefaultJson(objectMapper.writeValueAsString(formatData.getDefaultInfo()));
            format.setFormatJson(objectMapper.writeValueAsString(formatData.getFormatInfo()));
            format.setActive(Boolean.parseBoolean(active));
            formatService.updateFormat(format);
            return ResponseEntity.ok("포맷 갱신 성공");
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("JSON 파싱 실패: " + e.getMessage());
        }

    }
    @PostMapping("/remove")
    public ResponseEntity<String> removeFormat(@RequestParam String formatId){
        formatService.removeFormat(Long.parseLong(formatId));
        return ResponseEntity.ok("포맷 삭제 성공");
    }

}
