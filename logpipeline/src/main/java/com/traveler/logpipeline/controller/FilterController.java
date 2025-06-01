package com.traveler.logpipeline.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.logpipeline.dto.FilterRequestDto;
import com.traveler.logpipeline.dto.FilterResponseDto;
import com.traveler.logpipeline.entity.Filter;
import com.traveler.logpipeline.service.FilterService;
import com.traveler.logpipeline.service.FormatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/filter")
public class FilterController {
    private final FilterService filterService;
    private final FormatService formatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FilterController(FilterService filterService, FormatService formatService) {
        this.filterService = filterService;
        this.formatService = formatService;
    }

    @GetMapping("/list")
    public List<FilterResponseDto> getFilterList(@RequestParam String processId){
        return filterService.listFilters(Long.parseLong(processId));
    }
    @GetMapping("/view")
    public FilterResponseDto viewFilter(@RequestParam String filterId){
        return filterService.viewFilter(Long.parseLong(filterId));
    }
    @GetMapping("/keys")
    public List<String> getFormatList(@RequestParam String processId){
        return formatService.activeFormatKeys(Long.parseLong(processId));
    }
    @PostMapping("/add")
    public ResponseEntity<String> addFilter(@RequestParam String processId, @RequestParam String name, @RequestParam String active, @RequestBody FilterRequestDto data){
        LinkedHashMap<String, String> fields = extractFields(data.getTokens());
        String fullCode = generateCode(fields, data.getExpression());
        try {
            Filter filter = new Filter();
            filter.setName(name);
            filter.setActive(Boolean.parseBoolean(active));
            filter.setSourceCode(fullCode);
            filter.setTokensJson(objectMapper.writeValueAsString(data.getTokens()));
            filter.setUsedField(objectMapper.writeValueAsString(fields));
            filterService.addFilter(filter, Long.parseLong(processId));
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateFilter(@RequestParam String filterId, @RequestParam String name, @RequestParam String active, @RequestBody FilterRequestDto data){
        LinkedHashMap<String, String> fields = extractFields(data.getTokens());
        String fullCode = generateCode(fields, data.getExpression());
        try {
            Filter filter = new Filter();
            filter.setId(Long.parseLong(filterId));
            filter.setName(name);
            filter.setActive(Boolean.parseBoolean(active));
            filter.setSourceCode(fullCode);
            filter.setTokensJson(objectMapper.writeValueAsString(data.getTokens()));
            filter.setUsedField(objectMapper.writeValueAsString(fields));
            filterService.updateFilter(filter);
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/remove")
    public ResponseEntity<String> removeFilter(@RequestParam String filterId){
        filterService.removeFilter(Long.parseLong(filterId));
        return ResponseEntity.ok("필터 삭제 성공");
    }

    public LinkedHashMap<String, String> extractFields(List<Map<String, Object>> tokens){
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        for(Map<String, Object> token : tokens){
            if(token.get("type").equals("condition")){
                String field = token.get("field").toString();
                String valueType = token.get("valueType").toString();
                fields.put(field, valueType);
            }
        }
        return fields;
    }

    public String generateCode(LinkedHashMap<String, String> fields, String expr){
        StringBuilder argsCode = new StringBuilder();
        fields.forEach((arg, type) -> {
            argsCode.append(type);
            argsCode.append(" ");
            argsCode.append(arg);
            argsCode.append(", ");
        });
        String fullCode = """
            public boolean evaluate(%s){
                return (%s);
            }
        """.formatted(argsCode.substring(0, argsCode.length() - 2), expr);
        return fullCode;
    }

}
//list, view, add, update, remove
//format이랑 filter에서 boolean 칼럼 만들어서 적용 여부 설정 및 확인
