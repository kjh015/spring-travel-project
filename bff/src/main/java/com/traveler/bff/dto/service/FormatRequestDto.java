package com.traveler.bff.dto.service;

import lombok.Data;

import java.util.Map;

@Data
public class FormatRequestDto {
    private Map<String, String> formatInfo;
    private Map<String, String> defaultInfo;
}
