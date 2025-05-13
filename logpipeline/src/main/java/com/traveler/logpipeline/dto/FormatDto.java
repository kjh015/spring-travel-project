package com.traveler.logpipeline.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FormatDto {
    private Map<String, String> formatInfo;
    private Map<String, String> defaultInfo;
}
