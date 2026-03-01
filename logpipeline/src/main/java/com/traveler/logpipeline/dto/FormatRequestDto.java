package com.traveler.logpipeline.dto;

import java.util.Map;
import lombok.Data;

@Data
public class FormatRequestDto {
    private Map<String, String> formatInfo;
    private Map<String, String> defaultInfo;
}
