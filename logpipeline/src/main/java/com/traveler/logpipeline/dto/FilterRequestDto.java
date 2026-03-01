package com.traveler.logpipeline.dto;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class FilterRequestDto {
    private String expression; // 논리식 문자열
    private List<Map<String, Object>> tokens;
}
