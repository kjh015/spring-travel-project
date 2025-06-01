package com.traveler.logpipeline.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FilterRequestDto {
    private String expression;           // 논리식 문자열
    private List<Map<String, Object>> tokens;
}
